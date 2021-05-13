package uk.joshiejack.settlements.world.town.people;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.settlements.network.town.people.PacketSyncCustomNPCs;
import uk.joshiejack.settlements.network.town.people.PacketSyncResidents;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class CensusServer extends AbstractCensus implements INBTSerializable<NBTTagCompound> {
    private final Set<ResourceLocation> inviteList = Sets.newHashSet(); //SUB_SET of Residents SERVER ONLY
    private final Multimap<ResourceLocation, Action> memorableActions = HashMultimap.create(); //SERVER ONLY
    private final Map<ResourceLocation, Pair<ResourceLocation, NBTTagCompound>> customNPCs = Maps.newHashMap();
    private final Spawner spawner;
    private final TownServer town;

    public CensusServer(TownServer town) {
        this.town = town;
        this.spawner = new Spawner(town);
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public Collection<? extends Spawner.Worker> getWorkers() {
        return customNPCs.values().stream().map(Spawner.TempWorker::new).collect(Collectors.toList());
    }

    public Collection<ResourceLocation> getCustomNPCKeys() {
        return customNPCs.keySet();
    }

    public Collection<Pair<ResourceLocation, NBTTagCompound>> getCustomNPCs() {
        return customNPCs.values();
    }

    public void createCustomNPCFromData(World world, ResourceLocation uniqueID, NPC baseNPC, NBTTagCompound data) {
        if (!data.hasKey("UniqueID")) data.setString("UniqueID", uniqueID.toString());
        customNPCs.put(uniqueID, Pair.of(baseNPC.getRegistryName(), data));
        onNPCsChanged(world);
        AdventureDataLoader.get(world).markDirty();
        PenguinNetwork.sendToEveryone(new PacketSyncCustomNPCs(world.provider.getDimension(), town.getID(), customNPCs.values()));
    }

    public void invite(ResourceLocation npc) {
        inviteList.add(npc);
    }

    public void onNewDay(World world) {
        if (!inviteList.isEmpty()) {
            for (ResourceLocation uniqueID : inviteList) {
                NPC theNPC = customNPCs.containsKey(uniqueID) ? NPC.getNPCFromRegistry(customNPCs.get(uniqueID).getLeft()) : NPC.getNPCFromRegistry(uniqueID);
                NBTTagCompound theData = customNPCs.containsKey(uniqueID) ? customNPCs.get(uniqueID).getRight() : null;
                EntityNPC npcEntity = spawner.getNPC(world, theNPC, uniqueID, theData, town.getCentre());
                if (npcEntity != null) {
                    npcEntity.getPhysicalAI().addToHead((LinkedList<Action>) memorableActions.get(uniqueID).stream().filter(Action::isPhysical).collect(Collectors.toCollection(LinkedList::new)));
                    npcEntity.getMentalAI().addToHead((LinkedList<Action>) memorableActions.get(uniqueID).stream().filter(a -> !a.isPhysical()).collect(Collectors.toCollection(LinkedList::new)));
                    memorableActions.removeAll(uniqueID); //Clear
                }
            }

            inviteList.clear();
        }

        onNPCsChanged(world); //Resync
    }

    public void onNPCDeath(EntityNPC npc) {
        npc.getPhysicalAI().all().stream().filter(Action::isMemorable)
                .forEach(action -> memorableActions.get(npc.getInfo().getRegistryName()).add(action));
        npc.getMentalAI().all().stream().filter(Action::isMemorable)
                .forEach(action -> memorableActions.get(npc.getInfo().getRegistryName()).add(action));
    }

    public void onNPCsChanged(World world) {
        Set<ResourceLocation> original = Sets.newHashSet(invitableList);
        invitableList.clear();
        invitableList.addAll(residents);
        invitableList.addAll(customNPCs.keySet());
        spawner.getNearbyNPCs(world).forEach(e -> invitableList.remove(e.getInfo().getRegistryName()));
        invitableList.removeAll(inviteList); //If they are already invited remove them
        if (!original.equals(invitableList)) {
            PenguinNetwork.sendToTeam(new PacketSyncResidents(town.getID(), invitableList), world, town.getCharter().getTeamID());
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        residents.clear(); //Updo
        NBTTagList inviteTagList = tag.getTagList("InviteList", 8);
        for (int i = 0; i < inviteTagList.tagCount(); i++) {
            inviteList.add(new ResourceLocation(inviteTagList.getStringTagAt(i)));
        }
        //Action memory
        memorableActions.clear(); //Empty
        NBTTagList memorableList = tag.getTagList("MemorableList", 10);
        for (int i = 0; i < memorableList.tagCount(); i++) {
            NBTTagCompound memorableNBT = memorableList.getCompoundTagAt(i);
            ResourceLocation npc = new ResourceLocation(memorableNBT.getString("NPC"));
            NBTTagList actionList = memorableNBT.getTagList("Actions", 10);
            for (int j = 0; j < actionList.tagCount(); j++) {
                NBTTagCompound actionNBT = actionList.getCompoundTagAt(j);
                Action action = Action.createOfType(actionNBT.getString("Type"));
                action.deserializeNBT(actionNBT.getCompoundTag("Data"));
                memorableActions.get(npc).add(action);
            }
        }

        NBTTagList memorableDataList = tag.getTagList("CustomData", 10);
        for (int i = 0; i < memorableDataList.tagCount(); i++) {
            NBTTagCompound data = memorableDataList.getCompoundTagAt(i);
            ResourceLocation npc = new ResourceLocation(data.getString("NPC"));
            ResourceLocation base = new ResourceLocation(data.getString("Base"));
            customNPCs.put(npc, Pair.of(base, data.getCompoundTag("Data")));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList inviteTagList = new NBTTagList();
        inviteList.forEach(npc -> inviteTagList.appendTag(new NBTTagString(npc.toString())));
        tag.setTag("InviteList", inviteTagList);
        //Action memory
        NBTTagList memorableList = new NBTTagList();
        memorableActions.keySet().forEach((npc) -> {
            NBTTagCompound memorableNBT = new NBTTagCompound();
            memorableNBT.setString("NPC", npc.toString());
            NBTTagList actionList = new NBTTagList();
            for (Action action : memorableActions.get(npc)) {
                NBTTagCompound actionNBT = new NBTTagCompound();
                actionNBT.setString("Type", action.getType());
                actionNBT.setTag("Data", action.serializeNBT());
                actionList.appendTag(actionNBT);
            }

            memorableNBT.setTag("Actions", actionList);
            memorableList.appendTag(memorableNBT);
        });

        tag.setTag("MemorableList", memorableList);

        //Save the custom data
        NBTTagList memorableDataList = new NBTTagList();
        customNPCs.keySet().forEach(npc -> {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("NPC", npc.toString());
            data.setString("Base", customNPCs.get(npc).getLeft().toString());
            data.setTag("Data", customNPCs.get(npc).getRight());
            memorableDataList.appendTag(data);
        });

        tag.setTag("CustomData", memorableDataList);
        return tag;
    }
}

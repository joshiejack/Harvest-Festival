package joshie.harvest.town;

import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.gathering.GatheringData;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TownDataServer extends TownData {
    public GatheringData gathering = new GatheringData();
    protected Set<ResourceLocation> deadVillagers = new HashSet<>();

    public TownDataServer() {}
    public TownDataServer(BlockPos pos) {
        townCentre = pos;
        uuid = UUID.randomUUID();
    }

    public void markNPCDead(ResourceLocation name) {
        deadVillagers.add(name);
    }

    @Override
    public void newDay(World world) {
        gathering.newDay(world, buildings.values());
        for (ResourceLocation villager: deadVillagers) {
            NPC npc = NPCRegistry.REGISTRY.getObject(villager);
            AbstractEntityNPC entity = NPCHelper.getEntityForNPC(world, npc);
            entity.setPosition(townCentre.getX(), townCentre.getY(), townCentre.getZ());
            entity.resetSpawnHome();
            BlockPos pos = entity.getHomeCoordinates();
            entity.setPositionAndUpdate(pos.getX(), pos.getY() + 0.5, pos.getZ());
            world.spawnEntityInWorld(entity);
        }

        deadVillagers = new HashSet<>(); //Reset the dead villagers
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        gathering.readFromNBT(nbt);
        nbt.setTag("DeadVillagers", NBTHelper.writeResourceSet(deadVillagers));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        gathering.writeToNBT(nbt);
        deadVillagers = NBTHelper.readResourceSet(nbt.getTagList("DeadVillagers", 8));
    }
}

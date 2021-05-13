package uk.joshiejack.penguinlib.world.teams;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.events.TeamChangedEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketChangeTeam;
import uk.joshiejack.penguinlib.network.packet.PacketSyncPlayerStatuses;
import uk.joshiejack.penguinlib.network.packet.PacketSyncTeamMembers;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class PenguinTeams extends WorldSavedData {
    private static final String DATA_NAME = "Penguin-Teams";
    private final Map<UUID, UUID> memberOf = Maps.newHashMap(); //Player ID > Team ID
    private final Map<UUID, PenguinTeam> teams = Maps.newHashMap(); // Team ID > Data

    public PenguinTeams() { super(DATA_NAME); }
    public PenguinTeams(String name) {
        super(name);
    }

    public static PenguinTeams get(World world) {
        PenguinTeams instance = (PenguinTeams) world.loadData(PenguinTeams.class, DATA_NAME);
        if (instance == null) {
            instance = new PenguinTeams(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance;
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PenguinTeams.getTeamForPlayer(event.player).syncToPlayer(event.player); //Sync the info about this players team to them
        PenguinNetwork.sendToClient(new PacketSyncPlayerStatuses(event.player.getEntityData().getCompoundTag("PenguinStatuses")), event.player);
        PenguinNetwork.sendToClient(new PacketSyncTeamMembers(PenguinTeams.get(event.player.world).memberOf), event.player);
    }

    public int getMemberCount(UUID owner_id) {
        UUID team = memberOf.get(owner_id);
        return teams.get(team).members().size();
    }

    public void changeTeam(World world, UUID player, UUID newUUID) {
        UUID oldUUID = memberOf.getOrDefault(player, player);
        memberOf.put(player, newUUID);
        PenguinTeam oldTeam = teams.get(oldUUID);
        if (oldTeam != null) {
            oldTeam.members().remove(player);
            oldTeam.onChanged(world);
        }

        if (!teams.containsKey(newUUID)) {
            teams.put(newUUID, new PenguinTeam(newUUID));
        }

        PenguinTeam newTeam = teams.get(newUUID);
        newTeam.members().add(player);
        newTeam.onChanged(world);
        MinecraftForge.EVENT_BUS.post(new TeamChangedEvent(world, player, oldUUID, newUUID));
        PenguinNetwork.sendToEveryone(new PacketChangeTeam(player, oldUUID, newUUID));
        markDirty();
    }

    public NBTTagCompound getTeamData(UUID team) {
        return teams.get(team).getData();
    }

    public Collection<UUID> getTeamMembers(UUID team) {
        return teams.get(team).members();
    }

    public static PenguinTeam getTeamFromID(World world, UUID team) {
        return get(world).teams.get(team);
    }

    public static PenguinTeam getTeamForPlayer(World world, UUID uuid) {
        PenguinTeams data = get(world); //Load the serverdata
        if (!data.memberOf.containsKey(uuid)) {
            //data.teams.put(uuid, new PenguinTeam(uuid);
            //data.memberOf.put(uuid, uuid);
            data.changeTeam(world, uuid, uuid);
            data.markDirty();
        }

        return data.teams.get(data.memberOf.get(uuid));
    }

    public static PenguinTeam getTeamForPlayer(EntityPlayer player) {
        if (player.world.isRemote) return PenguinTeamsClient.getInstance(); //Client data
        return getTeamForPlayer(player.world, PlayerHelper.getUUIDForPlayer(player));
    }

    public UUID getTeamUUIDForPlayer(EntityPlayer player) {
        return getTeamForPlayer(player).getID();
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList data = nbt.getTagList("Teams", 10);
        for (int i = 0; i < data.tagCount(); i++) {
            NBTTagCompound tag = data.getCompoundTagAt(i);
            PenguinTeam team = new PenguinTeam(tag);
            teams.put(team.getID(), team);
            team.members().forEach(member -> memberOf.put(member, team.getID())); //Add the quick reference for members
        }
    }

    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagList data = new NBTTagList();
        for (Map.Entry<UUID, PenguinTeam> entry: teams.entrySet()) {
            data.appendTag(entry.getValue().serializeNBT());
        }

        compound.setTag("Teams", data);

        return compound;
    }
}

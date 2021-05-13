package uk.joshiejack.penguinlib.world.teams;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.events.TeamChangedOwnerEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSyncTeamData;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class PenguinTeam implements INBTSerializable<NBTTagCompound> {
    private boolean isClient;
    private NBTTagCompound data;
    private Set<UUID> members;
    private UUID teamUUID;
    private UUID owner;

    public PenguinTeam(UUID uuid) {
        this.teamUUID = uuid;
        this.members = Sets.newHashSet();
        this.data = new NBTTagCompound();
    }

    public void setClient() {
        this.isClient = true;
    }

    public boolean isClient() {
        return isClient;
    }

    public PenguinTeam onChanged(World world) {
        if (!members.contains(owner)) {
            this.owner = members.stream().findFirst().orElse(null); //Grab a new one, it can be null
            MinecraftForge.EVENT_BUS.post(new TeamChangedOwnerEvent(teamUUID, owner));
        }

        syncToTeam(world);
        return this;
    }

    public PenguinTeam(NBTTagCompound data) {
        this.deserializeNBT(data);
    }

    public Set<UUID> members() {
        return members;
    }

    @Nullable
    public UUID getOwner() {
        return owner;
    }

    public UUID getID() {
        return teamUUID;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public void syncToPlayer(EntityPlayer player) {
        PenguinNetwork.sendToClient(new PacketSyncTeamData(serializeNBT()), player);
    }

    public void syncToTeam(World world) {
        PenguinNetwork.sendToTeam(new PacketSyncTeamData(serializeNBT()), world, teamUUID);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("UUID", teamUUID.toString());
        compound.setTag("Data", data);
        NBTTagList list = new NBTTagList();
        members.forEach(uuid -> list.appendTag(new NBTTagString(uuid.toString())));
        compound.setTag("Members", list);
        if (owner != null) {
            compound.setString("Owner", owner.toString());
        }

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.teamUUID = UUID.fromString(compound.getString("UUID"));
        this.data = compound.getCompoundTag("Data");
        this.members = Sets.newHashSet();
        this.owner = compound.hasKey("Owner") ? UUID.fromString(compound.getString("Owner")) : null;
        NBTTagList list = compound.getTagList("Members", 8);
        for (int i = 0; i < list.tagCount(); i++) {
            members.add(UUID.fromString(list.getStringTagAt(i)));
        }

        if (this.owner == null) {
            this.owner = members.stream().findFirst().orElse(null);
            MinecraftForge.EVENT_BUS.post(new TeamChangedOwnerEvent(teamUUID, owner));
        }
    }
}

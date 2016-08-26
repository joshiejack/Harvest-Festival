package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.NPC;
import joshie.harvest.town.TownData;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

import static joshie.harvest.api.npc.INPC.Location.HOME;

public abstract class EntityNPCHuman<E extends EntityNPCHuman> extends EntityNPC<E> {
    protected BlockPos spawned;
    protected TownData homeTown;
    protected UUID townID;

    public EntityNPCHuman(World world) {
        super(world);
    }

    public EntityNPCHuman(World world, NPC npc) {
        super(world, npc);
    }

    public EntityNPCHuman(E entity) {
        super(entity);
    }

    public BlockPos getHomeCoordinates() {
        BlockPos home = NPCHelper.getHomeForEntity(this);
        if (home == null) home = spawned;
        return home;
    }

    @SuppressWarnings("unchecked")
    private <T extends TownData> T getHomeTown() {
        if (homeTown == null) {
            if (townID != null) homeTown = TownHelper.getTownByID(worldObj, townID);
            else {
                homeTown = TownHelper.getClosestTownToEntity(this);
                townID = homeTown.getID(); //Set the town id when we create one
            }
        }

        return (T) homeTown;
    }

    public void resetSpawnHome() {
        TownHelper.ensureTownExists(worldObj, new BlockPos(this)); //Force a town to exist near this entity
        this.homeTown = TownHelper.getClosestTownToEntity(this);
        //A town cannot exist without a builder
        if (this.homeTown == TownTracker.NULL_TOWN) this.setDead();
        else {
            this.townID = homeTown.getID();
            this.spawned = homeTown.getCoordinatesFor(getNPC().getLocation(HOME));
            if (this.spawned == null) {
                this.spawned = new BlockPos(this);
            }
        }
    }

    public void setSpawnHome(TownData data) {
        this.homeTown = data;
        if (this.homeTown == TownTracker.NULL_TOWN) this.setDead();
        else {
            this.townID = homeTown.getID();
            this.spawned = homeTown.getCoordinatesFor(getNPC().getLocation(HOME));
            if (this.spawned == null) {
                this.spawned = new BlockPos(this);
            }
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!worldObj.isRemote && this.posY >= -32D) {
            //Respawn a new bugger
            if (npc.respawns()) {
                this.<TownDataServer>getHomeTown().markNPCDead(getNPC().getRegistryName());
                HFTrackers.markDirty(worldObj); //Mark this npc as dead, ready for tomorrow to be reborn
            }
        }

        super.onDeath(cause);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        super.writeSpawnData(buf);
        ByteBufUtils.writeUTF8String(buf, townID.toString());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        super.readSpawnData(buf);
        townID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        spawned = NBTHelper.readBlockPos("Original", nbt);
        townID = NBTHelper.readUUID("Town", nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        NBTHelper.writeUUID("Town", nbt, townID);
        NBTHelper.writeBlockPos("Original", nbt, spawned);
    }
}

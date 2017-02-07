package joshie.harvest.npcs.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.npcs.entity.ai.EntityAISchedule;
import joshie.harvest.npcs.entity.ai.EntityAITalkingTo;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nonnull;
import java.util.UUID;

public abstract class EntityNPCHuman<E extends EntityNPCHuman> extends EntityNPC<E> {
    private BlockPos spawned;
    private UUID townID;
    private EntityAIPathing pathing;
    TownData homeTown;

    EntityNPCHuman(World world) {
        super(world);
    }

    EntityNPCHuman(World world, NPC npc) {
        super(world, npc);
    }

    EntityNPCHuman(E entity) {
        super(entity);
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        pathing = new EntityAIPathing(this);
        tasks.addTask(5, pathing);
        tasks.addTask(6, new EntityAISchedule(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
    }

    public EntityAIPathing getPathing() {
        return pathing;
    }

    public BlockPos getHomeCoordinates() {
        BlockPos home = NPCHelper.getHomeForEntity(this);
        if (home == null) home = spawned;
        return home;
    }

    @SuppressWarnings("unchecked")
     <T extends TownData> T getHomeTown() {
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
        this.homeTown = TownHelper.getClosestTownToEntity(this);
        this.townID = homeTown.getID();
        this.spawned = homeTown.getCoordinatesFor(getNPC().getHome());
        if (this.spawned == null) {
            this.spawned = new BlockPos(this);
        }
    }

    public void setSpawnHome(TownData data) {
        this.homeTown = data;
        if (this.homeTown == null) this.setDead();
        else {
            this.townID = homeTown.getID();
            this.spawned = homeTown.getCoordinatesFor(getNPC().getHome());
            if (this.spawned == null) {
                this.spawned = new BlockPos(this);
            }
        }
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
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
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source != DamageSource.outOfWorld) {
            addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0, true, false));
            if (source.getSourceOfDamage() instanceof EntityPlayer) {
                HFApi.player.getRelationsForPlayer(((EntityPlayer) source.getSourceOfDamage())).affectRelationship(npc.getUUID(), -10);
            }
        }

        return super.attackEntityFrom(source, amount);
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

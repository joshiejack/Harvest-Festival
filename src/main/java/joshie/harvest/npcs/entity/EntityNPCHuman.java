package joshie.harvest.npcs.entity;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.npcs.entity.ai.EntityAISchedule;
import joshie.harvest.npcs.entity.ai.EntityAITalkingTo;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import joshie.harvest.town.tracker.TownTrackerServer;
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

import javax.annotation.Nonnull;

import static joshie.harvest.npcs.HFNPCs.NPC_AI_DISTANCE;

public abstract class EntityNPCHuman<E extends EntityNPCHuman> extends EntityNPC<E> {
    private EntityAIPathing pathing;
    @SuppressWarnings("WeakerAccess")
    private TownDataServer homeTown;
    private int nullChecker;

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
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(NPC_AI_DISTANCE);
    }

    public EntityAIPathing getPathing() {
        return pathing;
    }

    @SuppressWarnings("unchecked")
    public TownDataServer getHomeTown() {
        if (homeTown == null) {
            homeTown = TownHelper.getClosestTownToEntity(this, false);
        } else if (homeTown == TownTrackerServer.NULL_TOWN) {
            nullChecker++;
            if (nullChecker %200 == 0) {
                homeTown = TownHelper.getClosestTownToEntity(this, false);
            }
        }

        return homeTown;
    }

    @Override
    public void setPath(TaskElement... tasks) {
        pathing.setPath(tasks);
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        if (!world.isRemote) {
            //Respawn a new bugger
            if (npc.respawns()) {
                this.<TownDataServer>getHomeTown().markNPCDead(getNPC().getResource(), new BlockPos(this));
                HFTrackers.markTownsDirty(); //Mark this npc as dead, ready for tomorrow to be reborn
            }
        }

        super.onDeath(cause);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (source != DamageSource.OUT_OF_WORLD) {
            addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0, true, false));
            if (source.getTrueSource() instanceof EntityPlayer) {
                HFApi.player.getRelationsForPlayer(((EntityPlayer) source.getTrueSource())).affectRelationship(npc, -10);
            }

            if (source == DamageSource.IN_WALL) {
                attemptTeleport(posX + world.rand.nextInt(20) - 10D, posY + world.rand.nextInt(3), posZ + world.rand.nextInt(20) - 10D);
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (pathing != null) {
            pathing.readFromNBT(nbt.getCompoundTag("Pathing"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (pathing != null) {
            nbt.setTag("Pathing", pathing.writeToNBT(new NBTTagCompound()));
        }
    }
}

package joshie.harvest.mining.entity;

import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.mining.MiningHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static joshie.harvest.mining.HFMining.ANIMALS_ON_EVERY_FLOOR;
import static joshie.harvest.mining.MiningHelper.*;

public class EntityDarkSheep extends EntityMob {
    public EntityDarkSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 0.6D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        super.onDeath(cause);
        EntityPlayer player = EntityHelper.getPlayerFromSource(cause);
        if (player != null) {
            player.addStat(HFAchievements.killSheep);
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        int floor = MiningHelper.getFloor((int)posX >> 4, (int) posY);
        return floor >= GOLD_FLOOR && (ANIMALS_ON_EVERY_FLOOR || (((floor + 6) % SHEEP_FLOORS == 0))) && EntityHelper.getEntities(EntityDarkChicken.class, this, 24D).size() < 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(@Nonnull BlockPos pos, @Nonnull Block blockIn) {
        playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    public float getEyeHeight() {
        return 0.95F * this.height;
    }
}

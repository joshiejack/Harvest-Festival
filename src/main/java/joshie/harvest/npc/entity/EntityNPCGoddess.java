package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.blocks.BlockFlower.FlowerType;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.ai.EntityAILookAtPlayer;
import joshie.harvest.npc.entity.ai.EntityAISwim;
import joshie.harvest.npc.entity.ai.EntityAITalkingTo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityNPCGoddess extends EntityNPC<EntityNPCGoddess> {
    private boolean flower;
    private int lastTalk = 1200;

    public EntityNPCGoddess(World world) {
        this(world, (NPC) HFNPCs.GODDESS);
    }

    public EntityNPCGoddess(World world, NPC npc) {
        super(world, npc);
        setSize(0.6F, (2F * npc.getHeight()));
        setEntityInvulnerable(true);
    }

    public EntityNPCGoddess(EntityNPCGoddess entity) {
        this(entity.worldObj, entity.npc);
        npc = entity.getNPC();
        lover = entity.lover;
    }

    @Override
    protected EntityNPCGoddess getNewEntity(EntityNPCGoddess entity) {
        return new EntityNPCGoddess(entity);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwim(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (isInWater()) {
            moveRelative(strafe, forward, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.800000011920929D;
            motionY *= 0.800000011920929D;
            motionZ *= 0.800000011920929D;
        } else if (isInLava()) {
            moveRelative(strafe, forward, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        } else {
            float f = 0.91F;

            if (onGround) {
                f = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            moveRelative(strafe, forward, onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            if (onGround) {
                f = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
            }

            moveEntity(motionX, motionY, motionZ);
            motionX *= (double) f;
            motionY *= (double) f;
            motionZ *= (double) f;
        }

        prevLimbSwingAmount = limbSwingAmount;
        limbSwingAmount = 0F;
        limbSwing = 0F;
    }

    public void setFlower() {
        flower = true;
    }

    @Override
    public void setTalking(EntityPlayer player) {
        super.setTalking(player);
        lastTalk = 600;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //Spawn Particles around the goddess
        for (int i = 0; i < 16; i++) {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + 0.1 * rand.nextFloat(), posY + 0.2 * rand.nextFloat(), posZ + 0.1 * rand.nextFloat(), 0, -0.05, 0);
        }

        if (!worldObj.isRemote) {
            if (!isTalking() && lastTalk > 0) {
                lastTalk--;

                if (lastTalk <= 0) {
                    if (flower) {
                        ItemHelper.spawnByEntity(this, HFCore.FLOWERS.getStackFromEnum(FlowerType.GODDESS));
                    }

                    setDead();
                }
            }
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        flower = nbt.getBoolean("Flower");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Flower", flower);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        super.writeSpawnData(buf);
        buf.writeBoolean(flower);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        super.readSpawnData(buf);
        flower = buf.readBoolean();

        for (int i = 0; i < 16; i++) {
            worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX * 0.2 * rand.nextFloat(), posY + 0.5 + 0.2 * rand.nextFloat(), posZ + 0.2 * rand.nextFloat(), 0, 0, 0);
        }

        worldObj.playSound(posX, posY, posZ, HFSounds.GODDESS_SPAWN, SoundCategory.NEUTRAL, 1F, 1F, true);
    }
}
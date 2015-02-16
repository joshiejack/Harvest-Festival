package joshie.harvestmoon.npc;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityNPC extends EntityAgeable implements IEntityAdditionalSpawnData {
    protected NPC npc;
    protected double homeX, homeY, homeZ;
    protected EntityNPC lover;
    private EntityPlayer talkingTo;
    private boolean isPlaying;

    public EntityNPC(EntityNPC entity) {
        this(entity.worldObj, entity.npc, entity.homeX, entity.homeY, entity.homeZ);
        lover = entity.lover;
    }

    public EntityNPC(World world) {
        this(world, HMNPCs.goddess);
    }

    public EntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;

        this.setSize(0.6F, (1.8F * npc.getHeight()));
        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITalkingTo(this));
        this.tasks.addTask(1, new EntityAILookAtPlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    public EntityNPC(World world, NPC npc, double x, double y, double z) {
        this(world, npc);
        setPosition(x, y, z);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);

        homeX = x;
        homeY = y;
        homeZ = z;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(HMModInfo.MODPATH + ":" + "textures/entity/" + npc.getUnlocalizedName() + ".png");
    }

    public NPC getNPC() {
        return npc;
    }

    public EntityNPC getLover() {
        return lover;
    }

    @Override
    public String getCommandSenderName() {
        return npc.getLocalizedName();
    }

    @Override
    protected void updateEntityActionState() {
        if (!isTalking()) {
            super.updateEntityActionState();
        } else {
            addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
        }
    }

    public boolean isTalking() {
        return talkingTo == null;
    }

    public void setTalking(EntityPlayer player) {
        talkingTo = player;
    }

    public EntityPlayer getTalkingTo() {
        return talkingTo;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    
    @Override
    public boolean isChild() {
        return npc.isChild();
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPC clone = new EntityNPC(this);
            worldObj.spawnEntityInWorld(clone);
            for (int i = 0; i < 20; ++i) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                clone.worldObj.spawnParticle("explode", clone.posX + (double) (clone.rand.nextFloat() * clone.width * 2.0F) - (double) clone.width, clone.posY + (double) (clone.rand.nextFloat() * clone.height), clone.posZ + (double) (clone.rand.nextFloat() * clone.width * 2.0F) - (double) clone.width, d2, d0, d1);
            }
        }

        super.setDead();
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack held = player.inventory.getCurrentItem();
        boolean flag = held != null && held.getItem() == Items.spawn_egg;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                player.openGui(HarvestMoon.instance, npc.getGuiID(worldObj, player.isSneaking() && held != null), worldObj, getEntityId(), 0, 0);
                setTalking(player);
            }

            return true;
        } else {
            return super.interact(player);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        npc = HMNPCs.get(nbt.getString("NPC"));
        homeX = nbt.getDouble("HomeX");
        homeY = nbt.getDouble("HomeY");
        homeZ = nbt.getDouble("HomeZ");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) {
            nbt.setString("NPC", npc.getUnlocalizedName());
        }

        nbt.setDouble("HomeX", homeX);
        nbt.setDouble("HomeY", homeY);
        nbt.setDouble("HomeZ", homeZ);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        if (npc != null) {
            char[] name = npc.getUnlocalizedName().toCharArray();
            buf.writeByte(name.length);
            for (char c : name) {
                buf.writeChar(c);
            }
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        char[] name = new char[buf.readByte()];
        for (int i = 0; i < name.length; i++) {
            name[i] = buf.readChar();
        }

        npc = HMNPCs.get(new String(name));
        if (npc == null) {
            npc = HMNPCs.goddess;
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityNPC(worldObj, HMNPCs.goddess);
    }
}
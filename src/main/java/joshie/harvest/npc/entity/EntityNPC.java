package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ai.INPCTask;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableProvider;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.ai.EntityAINPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.UUID;

public class EntityNPC extends EntityAgeable implements IEntityAdditionalSpawnData, IRelatableProvider {
    private boolean init;
    private INPCTask task; //Currently executing task
    protected INPC npc;
    protected EntityNPC lover;
    private EntityPlayer talkingTo;
    private boolean isPlaying;
    private Mode mode = Mode.DEFAULT;
    public UUID owning_player;
    public int lastTeleport;
    public boolean hideName = true;

    public enum Mode {
        DEFAULT, GIFT;
    }

    public EntityNPC(UUID owning_player, EntityNPC entity) {
        this(owning_player, entity.worldObj, entity.npc);
        lover = entity.lover;
    }

    public EntityNPC(World world) {
        this(null, world, HFNPCs.GODDESS);
    }

    @Override
    public IRelatable getRelatable() {
        return npc;
    }

    public INPCTask getTask() {
        return task;
    }

    public void setTask(INPCTask task) {
        this.task = task;
    }

    @Override
    public void onUpdate() {
        if (!joshie.harvest.core.config.NPC.FREEZE_NPC) {
            super.onUpdate();
        }
    }

    public EntityNPC(UUID owning_player, World world, INPC npc) {
        super(world);
        this.npc = npc;

        this.enablePersistence();
        this.owning_player = owning_player;
        setSize(0.6F, (1.8F * npc.getHeight()));
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);

        if (owning_player != null) {
            tasks.addTask(0, new EntityAISwimming(this));
            tasks.addTask(1, new EntityAIAvoidEntity<EntityZombie>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
            tasks.addTask(1, new EntityAITalkingTo(this));
            tasks.addTask(1, new EntityAILookAtPlayer(this));
            tasks.addTask(2, new EntityAITeleportHome(this));
            tasks.addTask(2, new EntityAINPC(this));
            tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
            tasks.addTask(4, new EntityAIOpenDoor(this, true));
            tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
            tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
            tasks.addTask(9, new EntityAIWander(this, 0.6D));
            tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(HFModInfo.MODID + ":" + "textures/entity/" + npc.getUnlocalizedName() + ".png");
    }

    public INPC getNPC() {
        return npc;
    }

    public EntityNPC getLover() {
        return lover;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public String getName() {
        return npc.getLocalizedName();
    }

    @Override
    protected void updateAITasks() {
        if (!init) {
            init = true;
            initEntityAI();
        }

        updateTasks();
    }

    protected void updateTasks() {
        if (this.lastTeleport > 0) {
            this.lastTeleport--;
        }

        if (!isTalking()) {
            super.updateAITasks();
        } else {
            addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0, true, false));
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
    public boolean canDespawn() {
        return false;
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns() && !isDead) {
            EntityNPC clone = new EntityNPC(owning_player, this);
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getHeldItem(hand);
        boolean flag = held != null && held.getItem() == Items.SPAWN_EGG;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                player.openGui(HarvestFestival.instance, NPCHelper.getGuiIDForNPC(npc, worldObj, player, player.isSneaking() && held != null), worldObj, getEntityId(), -1, player.getActiveHand().ordinal());
                setTalking(player);
            }

            return true;
        } else {
            return super.processInteract(player, hand, stack);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        npc = HFApi.NPC.get(nbt.getString("NPC"));
        if (nbt.hasKey("Owner")) {
            owning_player = UUID.fromString(nbt.getString("Owner"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) {
            nbt.setString("NPC", npc.getUnlocalizedName());
        }

        if (owning_player != null) {
            nbt.setString("Owner", owning_player.toString());
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        if (npc != null) {
            char[] name = npc.getUnlocalizedName().toCharArray();
            buf.writeByte(name.length);
            for (char c : name) {
                buf.writeChar(c);
            }

            if (owning_player != null) {
                buf.writeBoolean(true);
                buf.writeLong(owning_player.getMostSignificantBits());
                buf.writeLong(owning_player.getLeastSignificantBits());
            } else buf.writeBoolean(false);
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        char[] name = new char[buf.readByte()];
        for (int i = 0; i < name.length; i++) {
            name[i] = buf.readChar();
        }

        npc = HFApi.NPC.get(new String(name));
        if (npc == null) {
            npc = HFNPCs.GODDESS;
        }

        if (buf.readBoolean()) {
            long most = buf.readLong();
            long least = buf.readLong();
            owning_player = new UUID(most, least);
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityNPC(owning_player, worldObj, HFNPCs.GODDESS);
    }
}
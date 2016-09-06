package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableProvider;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.ai.*;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import static joshie.harvest.core.handlers.GuiHandler.GIFT;

public abstract class EntityNPC<E extends EntityNPC> extends EntityAgeable implements IEntityAdditionalSpawnData, IRelatableProvider {
    protected AbstractTask task; //Currently executing task
    protected NPC npc;
    protected EntityNPC lover;
    protected EntityPlayer talkingTo;
    protected Mode mode = Mode.DEFAULT;

    public enum Mode {
        DEFAULT, GIFT
    }

    public EntityNPC(World world) {
        this(world, (NPC) HFNPCs.MAYOR);
    }

    public EntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;
        this.enablePersistence();
        setSize(0.6F, (1.8F * npc.getHeight()));
    }

    public EntityNPC(E entity) {
        this(entity.worldObj, entity.npc);
        npc = entity.getNPC();
        lover = entity.lover;
    }

    protected abstract E getNewEntity(E entity);

    @Override
    public IRelatable getRelatable() {
        return npc;
    }

    public AbstractTask getTask() {
        return task;
    }

    public void setTask(AbstractTask task) {
        this.task = task;
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAITask(this));
        tasks.addTask(6, new EntityAISchedule(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
        tasks.addTask(9, new EntityAIWander(this, 0.6D));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    public NPC getNPC() {
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

    public boolean isTalking() {
        return talkingTo != null;
    }

    public void setTalking(EntityPlayer player) {
        talkingTo = player;
    }

    public EntityPlayer getTalkingTo() {
        return talkingTo;
    }

    @Override
    public boolean isChild() {
        return npc.getAge() == INPCRegistry.Age.CHILD;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    public void resetSpawnHome() {}

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getHeldItem(hand);
        boolean flag = held != null && held.getItem() == Items.SPAWN_EGG;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                int guiID = NPCHelper.getGuiIDForNPC(npc, worldObj, player, player.isSneaking() && held != null);
                int third = guiID == GIFT ? player.getActiveHand().ordinal() : -1;
                player.openGui(HarvestFestival.instance, guiID, worldObj, getEntityId(), -1, third);
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
        npc = NPCRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("NPC")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) {
            nbt.setString("NPC", npc.getRegistryName().toString());
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeBoolean(npc != null);
        if (npc != null) {
            ByteBufUtils.writeUTF8String(buf, npc.getRegistryName().toString());
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        String name = buf.readBoolean() ? ByteBufUtils.readUTF8String(buf) : "";
        npc = name.equals("") ? (NPC) HFNPCs.MAYOR : NPCRegistry.REGISTRY.getValue(new ResourceLocation(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return getNewEntity((E)ageable);
    }
}
package joshie.harvest.npcs.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPCHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.NPCHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EntityNPC<E extends EntityNPC> extends EntityAgeable implements IEntityAdditionalSpawnData {
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
        setPathPriority(PathNodeType.WATER, 0.0F);
        stepHeight = 0.7F;
    }

    public EntityNPC(E entity) {
        this(entity.worldObj, entity.npc);
        npc = entity.getNPC();
        lover = entity.lover;
    }

    protected abstract E getNewEntity(E entity);

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    public boolean isBusy() {
        return false;
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

    @Nullable
    public EntityPlayer getTalkingTo() {
        return talkingTo;
    }

    @Override
    public boolean isChild() {
        return npc.getAge() == INPCHelper.Age.CHILD;
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
                int guiID = NPCHelper.getGuiIDForNPC(this, worldObj, player);
                player.openGui(HarvestFestival.instance, guiID, worldObj, getEntityId(), -1, -1);
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
        npc = NPC.REGISTRY.getValue(new ResourceLocation(nbt.getString("NPC")));
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
        npc = name.equals("") ? (NPC) HFNPCs.MAYOR : NPC.REGISTRY.getValue(new ResourceLocation(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return getNewEntity((E)ageable);
    }
}
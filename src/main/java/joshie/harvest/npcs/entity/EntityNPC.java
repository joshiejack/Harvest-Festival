package joshie.harvest.npcs.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPCHelper;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.town.Town;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityNPC<E extends EntityNPC> extends EntityAgeable implements IEntityAdditionalSpawnData, NPCEntity {
    protected UUID home;
    protected NPC npc;
    private EntityPlayer talkingTo;
    private Mode mode = Mode.DEFAULT;
    EntityNPC lover;

    public enum Mode {
        DEFAULT, GIFT
    }

    public EntityNPC(World world) {
        this(world, HFNPCs.MAYOR);
    }

    public EntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;
        this.enablePersistence();
        setSize(0.6F, (1.6F * npc.getHeight()));
        setPathPriority(PathNodeType.WATER, -1.0F);
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

    public UUID getHome() {
        if (home != null) return home;
        else return home = TownHelper.getClosestTownToEntity(this, false).getID();
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
    public BlockPos getPos() {
        return new BlockPos(this);
    }

    @Override
    public Town getTown() {
        return TownHelper.getClosestTownToEntity(this, false);
    }

    @Override
    public void setPath(TaskElement... tasks) {}

    @Override
    public EntityAgeable getAsEntity() {
        return this;
    }

    @Override
    @Nonnull
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

    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getHeldItem(hand);
        boolean flag = held != null && held.getItem() == Items.SPAWN_EGG;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                int guiID = NPCHelper.getGuiIDForNPC(this, worldObj, player);
                player.openGui(HarvestFestival.instance, guiID, worldObj, getEntityId(), -1, -1);
            }

            return true;
        } else {
            return super.processInteract(player, hand, stack);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        npc = NPC.REGISTRY.get(new ResourceLocation(nbt.getString("NPC")));
        if (nbt.hasKey("Town")) {
            home = UUID.fromString("Town");
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) nbt.setString("NPC", npc.getResource().toString());
        if (home != null) nbt.setString("Town", home.toString());
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeBoolean(npc != null);
        if (npc != null) {
            ByteBufUtils.writeUTF8String(buf, npc.getResource().toString());
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        String name = buf.readBoolean() ? ByteBufUtils.readUTF8String(buf) : "";
        npc = name.equals("") ? HFNPCs.MAYOR : NPC.REGISTRY.get(new ResourceLocation(name));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return getNewEntity((E)ageable);
    }
}
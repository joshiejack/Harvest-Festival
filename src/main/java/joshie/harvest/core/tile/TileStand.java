package joshie.harvest.core.tile;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileStand<Q extends Quest> extends TileFaceable {
    private UUID owner;
    protected ItemStack stack;
    private Festival festival;

    @SuppressWarnings("WeakerAccess")
    public TileStand(Festival festival) {
        this.festival = festival;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Q getQuest() {
        return (Q) TownHelper.getClosestTownToBlockPos(worldObj, pos, false).getQuests().getAQuest(festival.getQuest());
    }

    protected boolean isQuestInvalid(Q quest) {
        return quest == null;
    }

    public boolean isEmpty() {
        return stack == null;
    }

    protected void onContentsSet(@Nullable EntityPlayer player, @Nonnull Q quest) {}

    protected void onContentsRemoved(@Nonnull Q quest) {}

    public void setContents(ItemStack stack) {
        this.stack = stack;
        this.markDirty();
    }

    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        Q quest = getQuest();
        if (isQuestInvalid(quest) || this.stack != null) return false;
        else {
            onContentsSet(player, quest);
            this.stack = stack.splitStack(1); //Remove one item
            MCServerHelper.markTileForUpdate(this);
        }

        return true;
    }

    public ItemStack removeContents() {
        Q quest = getQuest();
        if (stack == null) return null;
        else {
            if (quest != null) onContentsRemoved(quest);
            ItemStack stack = this.stack.copy();
            this.stack = null;
            MCServerHelper.markTileForUpdate(this);
            return stack;
        }
    }

    public ItemStack getContents() {
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = nbt.hasKey("Stack") ? NBTHelper.readItemStack(nbt.getCompoundTag("Stack")) : null;
        owner = nbt.hasKey("Owner") ? UUID.fromString("Owner") : null;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (stack != null) nbt.setTag("Stack", NBTHelper.writeItemStack(stack, new NBTTagCompound()));
        if (owner != null) nbt.setString("Owner", owner.toString());
        return super.writeToNBT(nbt);
    }
}

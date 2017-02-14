package joshie.harvest.core.tile;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileStandContest<Q extends Quest> extends TileStand {
    protected ItemStack stack;
    private Festival festival;

    @SuppressWarnings("WeakerAccess")
    public TileStandContest(Festival festival) {
        this.festival = festival;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Q getQuest() {
        return (Q) TownHelper.getClosestTownToBlockPos(worldObj, pos, false).getQuests().getAQuest(festival.getQuest());
    }

    protected boolean isQuestInvalid(Q quest) {
        return false;
    }

    protected void onContentsSet(@Nullable EntityPlayer player, @Nonnull Q quest) {}
    protected void onContentsRemoved(@Nonnull Q quest) {}

    @Override
    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        Q quest = getQuest();
        if (quest == null || isQuestInvalid(quest) || this.stack != null) return false;
        else {
            onContentsSet(player, quest);
            this.stack = stack.splitStack(1); //Remove one item
            MCServerHelper.markTileForUpdate(this);
        }

        return true;
    }

    @Override
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
}

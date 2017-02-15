package joshie.harvest.core.tile;

import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.town.festivals.QuestContestCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCookingStand extends TileStandContest<QuestContestCooking> {
    public TileCookingStand() {
        super(HFFestivals.COOKING_CONTEST);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack held) {
        return held.getItem() instanceof ItemFood;
    }

    @Override
    protected void onContentsSet(@Nullable EntityPlayer player, @Nonnull QuestContestCooking quest) {
        if (player != null) quest.addStand(EntityHelper.getPlayerUUID(player), pos);
    }

    @Override
    protected void onContentsRemoved(@Nonnull QuestContestCooking quest) {
        quest.removeStand(pos);
    }

    @Override
    protected boolean isQuestInvalid(QuestContestCooking quest) {
        return quest.isFull();
    }
}

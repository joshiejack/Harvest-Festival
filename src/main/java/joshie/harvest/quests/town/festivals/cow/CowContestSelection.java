package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.quests.town.festivals.QuestContestCow;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nullable;

public class CowContestSelection extends Selection<QuestContestCow> {
    public CowContestSelection() {
        super("harvestfestival.quest.festival.cow.pick", "harvestfestival.quest.festival.cow.how", "harvestfestival.quest.festival.cow.ready", "harvestfestival.quest.festival.cow.cancel");
    }

    @Override
    public Result onSelected(EntityPlayer player, EntityLiving entity, NPC npc, @Nullable QuestContestCow quest, int option) {
        if (quest == null) {
            return Result.DENY;
        } else if (option == 1) {
            quest.setStage(QuestContestCow.EXPLAIN);
            quest.syncData(player);
            return Result.ALLOW;
        } else if (option == 2) {
            quest.setStage(QuestContestCow.SELECT);
            quest.syncData(player);
            return Result.ALLOW;
        } else {
            quest.setStage(QuestContestCow.QUESTION);
            quest.syncData(player);
            return Result.DENY;
        }
    }
}

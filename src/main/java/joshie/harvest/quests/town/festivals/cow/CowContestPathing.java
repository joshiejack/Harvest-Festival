package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.schedule.ScheduleMove;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.npc.schedule.ScheduleWait;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.town.festivals.QuestContestCow;
import joshie.harvest.quests.town.festivals.contest.ScheduleWinner;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;

import static joshie.harvest.town.BuildingLocations.*;

public class CowContestPathing {
    private static ScheduleMove getMove(TownData town, BuildingLocation location) {
        return ScheduleMove.of(town.getCoordinatesFor(location));
    }

    public static void execute(EntityPlayer player, QuestContestCow quest, EntityAIPathing pathing) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        quest.getEntries().startContest(player); //Spawn any relevant data
        quest.setStage(QuestContestCow.START); //Mark as having started
        quest.syncData(player); //Sync up to the client
        pathing.setPath(getMove(town, PARK_COW_1), ScheduleSpeech.of(QuestContestCow.COW_JUDGE_1), getMove(town, PARK_COW_2), ScheduleSpeech.of(QuestContestCow.COW_JUDGE_2),
                getMove(town, PARK_COW_3), ScheduleSpeech.of(QuestContestCow.COW_JUDGE_3), getMove(town, PARK_COW_4), ScheduleSpeech.of(QuestContestCow.COW_JUDGE_4), ScheduleWait.of(1),
                ScheduleSpeech.of(QuestContestCow.COW_FINISH), getMove(town, PARK_COW_JUDGE), ScheduleSpeech.of(QuestContestCow.COW_WINNER), new ScheduleWinner(HFFestivals.COW_FESTIVAL));
    }

}

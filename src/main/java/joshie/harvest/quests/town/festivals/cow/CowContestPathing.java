package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleElement;
import joshie.harvest.api.npc.schedule.ScheduleMove;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.npc.schedule.ScheduleWait;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.core.base.other.HFScript;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.quests.town.festivals.QuestContestCow;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import joshie.harvest.quests.town.festivals.contest.ContestEntry;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

import static joshie.harvest.quests.town.festivals.Place.*;
import static joshie.harvest.town.BuildingLocations.*;

public class CowContestPathing {
    private static final Script cowJudge1 = new CowContestJudgingScript(1);
    private static final Script cowJudge2 = new CowContestJudgingScript(2);
    private static final Script cowJudge3 = new CowContestJudgingScript(3);
    private static final Script cowJudge4 = new CowContestJudgingScript(4);
    private static final Script cowFinish = new HFScript("cow_finish");
    private static final Script cowWinner = new HFScript("cow_winner") {
        @Override
        public String getLocalized(EntityAgeable ageable, NPC npc) {
            QuestContestCow quest = TownHelper.getClosestTownToEntity(ageable, false).getQuests().getAQuest(HFFestivals.COW_FESTIVAL.getQuest());
            ContestEntries entries = quest.getEntries();
            World world = ageable.worldObj;
            ContestEntry third = entries.getEntry(THIRD);
            ContestEntry second = entries.getEntry(SECOND);
            ContestEntry first = entries.getEntry(FIRST);
            return StringEscapeUtils.unescapeJava(I18n.translateToLocalFormatted(unlocalised, third.getOwnerName(), third.getEntityName(world),
                    second.getOwnerName(), second.getEntityName(world),
                    first.getOwnerName(), first.getEntityName(world)));
        }
    };

    private static ScheduleMove getMove(TownData town, BuildingLocation location) {
        return ScheduleMove.of(town.getCoordinatesFor(location));
    }

    public static void execute(EntityPlayer player, QuestContestCow quest, EntityAIPathing pathing) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        quest.getEntries().startContest(player); //Spawn any relevant data
        quest.setStage(QuestContestCow.START); //Mark as having started
        quest.syncData(player); //Sync up to the client
        ScheduleWinner winner = new ScheduleWinner(quest, player);
        pathing.setPath(getMove(town, PARK_COW_1), ScheduleSpeech.of(cowJudge1), getMove(town, PARK_COW_2), ScheduleSpeech.of(cowJudge2),
                getMove(town, PARK_COW_3), ScheduleSpeech.of(cowJudge3), getMove(town, PARK_COW_4), ScheduleSpeech.of(cowJudge4), ScheduleWait.of(1),
                ScheduleSpeech.of(cowFinish), getMove(town, PARK_COW_JUDGE), ScheduleSpeech.of(cowWinner), winner);
    }

    private static class ScheduleWinner extends ScheduleElement {
        private final QuestAnimalContest quest;
        private final EntityPlayer player;

        @SuppressWarnings("WeakerAccess")
        public ScheduleWinner(QuestAnimalContest quest, EntityPlayer player) {
            this.quest = quest;
            this.player = player;
        }

        public void execute(EntityAgeable npc) {
            super.execute(npc);
            ContestEntries entries = quest.getEntries();
            for (Place place: Place.VALUES) {
                entries.getEntry(place).reward(player.worldObj, place, quest.getEntries().getNPCs(), quest.getReward(place));
            }

            entries.kill(npc.worldObj);
            quest.complete(player);
        }
    }
}

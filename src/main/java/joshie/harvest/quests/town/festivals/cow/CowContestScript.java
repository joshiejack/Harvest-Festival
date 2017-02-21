package joshie.harvest.quests.town.festivals.cow;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.town.festivals.QuestContestCow;
import joshie.harvest.quests.town.festivals.QuestContestCow.ScheduleWinner;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CowContestScript extends Script {
    private final int cowID;

    public CowContestScript(int id) {
        super(new ResourceLocation(MODID, "cow_judge_" + id));
        setNPC(HFNPCs.MILKMAID);
        cowID = id; //get the correct id
    }

    private String getTextFromScore(int score) {
        if (score <= 0) return "This cow is awful, we need a vet!";
        else if (score <= 3000) return "This cow looks alright.";
        else if (score <= 6000) return "This cow seems decent.";
        else if (score <= 9000) return "This cow is starting to look good.";
        else if (score <= 12000) return "This cow is looking pretty good.";
        else if (score <= 15000) return "This cow is looking good, for sure!.";
        else if (score <= 18000) return "This cow is very good! The owner should be proud";
        else if (score <= 21000) return "This cow doing incredibly well. They sure love their owner.";
        else if (score <= 24000) return "This is doing awesome. Their owner is great.";
        else return "This cow is aboslutely outstanding. Fantastic coat!";
    }

    @Override
    public String getLocalized(EntityAgeable ageable, NPC npc) {
        QuestContestCow quest = TownHelper.getClosestTownToEntity(ageable, false).getQuests().getAQuest(HFFestivals.COW_FESTIVAL.getQuest());
        if (quest != null) {
            AnimalContestEntry entry;
            EntityHarvestCow cow = CowSelection.getClosestCow(ageable.worldObj, new BlockPos(ageable));
            if (cow != null) {
                if (quest.isPlayersCow(cow)) {
                    entry = new AnimalContestEntry(null, cow);
                } else entry = ScheduleWinner.getEntries(ageable.worldObj, quest).getEntry(cowID);
                return TextHelper.format("Let's take a look at '" + cow.getName() + "'. Well... %s", getTextFromScore(entry.getScore()));
            } else return "There appears to be no cow at this stall";
        } else return "INVALID";
    }
}

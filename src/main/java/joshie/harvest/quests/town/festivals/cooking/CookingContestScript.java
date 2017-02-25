package joshie.harvest.quests.town.festivals.cooking;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.calendar.HFFestivals;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.town.festivals.QuestContestCooking;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingContestScript extends Script {
    public CookingContestScript() {
        super(new ResourceLocation(MODID, "cooking"));
        setNPC(HFNPCs.GS_OWNER);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getLocalized(NPCEntity entity) {
        QuestContestCooking quest = TownHelper.getClosestTownToEntity(entity.getAsEntity(), false).getQuests().getAQuest(HFFestivals.COOKING_CONTEST.getQuest());
        if (quest != null) {
            return I18n.translateToLocalFormatted(unlocalised, quest.getContestEntries(entity).getLocalization());
        } else return "INVALID";
    }
}

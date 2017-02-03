package joshie.harvest.festivals.cooking;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingContestScript extends Script {
    public CookingContestScript() {
        super(new ResourceLocation(MODID, "cooking"));
        setNPC(HFNPCs.GS_OWNER);
    }

    @Override
    public String getLocalized(EntityAgeable ageable, NPC npc) {
        CookingContestQuest quest = TownHelper.getClosestTownToEntity(ageable).getQuests().getAQuest(CookingContest.FESTIVAL.getQuest());
        if (quest != null) {
            return I18n.translateToLocalFormatted(unlocalised, quest.getWinner(ageable).getLocalization());
        } else return "INVALID";
    }
}

package joshie.harvest.quests.town.festivals.harvest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.quests.town.festivals.QuestHarvestFestival;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ScriptHarvestDecide extends Script {
    public ScriptHarvestDecide() {
        super(new ResourceLocation(MODID, "harvest"));
        unlocalised = "harvestfestival.quest.festival.harvest.decide.";
    }

    @Override
    public String getLocalized(NPCEntity entity) {
        int score = QuestHarvestFestival.getPotScore(entity);
        return I18n.translateToLocalFormatted(unlocalised + score);
    }
}

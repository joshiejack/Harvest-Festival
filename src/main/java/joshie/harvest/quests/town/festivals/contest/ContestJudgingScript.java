package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ContestJudgingScript extends Script {
    private final int id;

    public ContestJudgingScript(String animal, int id) {
        super(new ResourceLocation(MODID, animal + "_judge_" + id));
        this.setNPC(HFNPCs.MILKMAID);
        this.unlocalised = getRegistryName().getResourceDomain() + ".script." + getRegistryName().getResourcePath().replace("_", ".").replace("_" + id, "");
        this.id = id; //get the correct id
    }

    private String getTextFromScore(int score) {
        return TextHelper.translate(unlocalised + "." + Math.max(0, Math.min(9, (int)Math.floor(((double)score) / 3000))));
    }

    @Override
    public String getLocalized(NPCEntity entity) {
        TownData data = TownHelper.getClosestTownToEntity(entity.getAsEntity(), false);
        QuestAnimalContest quest = data.getQuests().getAQuest(data.getFestival().getQuest());
        if (quest != null) {
            ContestEntry entry = quest.getEntries().getEntryFromStall(id);
            if (entry != null) {
                EntityAnimal animal = entry.getAnimalEntity(entity.getAsEntity().getEntityWorld());
                if (animal != null) {
                    return TextHelper.format(unlocalised, animal.getName(), getTextFromScore(entry.getScore(entity.getAsEntity().getEntityWorld())));
                }
            }
        }

        return TextHelper.translate(unlocalised + ".none");
    }
}

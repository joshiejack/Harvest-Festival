package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ContestJudgingScript extends Script {
    private final int id;

    public ContestJudgingScript(String prefix, int id) {
        super(new ResourceLocation(MODID, prefix + "_judge_" + id));
        this.unlocalised = getRegistryName().getResourceDomain() + ".script." + getRegistryName().getResourcePath().replace("_", ".").replace("_" + id, "") + ".judge";
        this.id = id; //get the correct id
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getLocalized(NPCEntity entity) {
        TownData data = TownHelper.getClosestTownToEntity(entity.getAsEntity(), false);
        QuestContest quest = data.getQuests().getAQuest(data.getFestival().getQuest());
        if (quest != null) {
            ContestEntry entry = quest.getEntries().getEntryFromStall(id);
            if (entry != null && !entry.isInvalid(entity.getAsEntity().getEntityWorld())) {
                return TextHelper.format(unlocalised, entry.getName(entity.getAsEntity().getEntityWorld()), entry.getTextFromScore(unlocalised, entry.getScore(quest, entity.getAsEntity().getEntityWorld())));
            }
        }

        return TextHelper.translate(unlocalised + ".none");
    }
}

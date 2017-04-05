package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.quests.town.festivals.Place.*;

public class ContestWinningScript extends Script {
    public ContestWinningScript(String name) {
        super(new ResourceLocation(MODID, name + "_winner"));
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getLocalized(NPCEntity entity) {
        TownData data = TownHelper.getClosestTownToEntity(entity.getAsEntity(), false);
        QuestContest quest = data.getQuests().getAQuest(data.getFestival().getQuest());
        ContestEntries entries = quest.getEntries();
        World world = entity.getAsEntity().getEntityWorld();
        ContestEntry third = entries.getEntry(THIRD);
        ContestEntry second = entries.getEntry(SECOND);
        ContestEntry first = entries.getEntry(FIRST);
        return StringEscapeUtils.unescapeJava(I18n.translateToLocalFormatted(unlocalised, third.getOwnerName(world), third.getName(world),
                second.getOwnerName(world), second.getName(world),
                first.getOwnerName(world), first.getName(world)));
    }
}

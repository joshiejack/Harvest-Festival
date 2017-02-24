package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.EntityAgeable;
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
    public String getLocalized(EntityAgeable ageable, NPC npc) {
        TownData data = TownHelper.getClosestTownToEntity(ageable, false);
        QuestAnimalContest quest = data.getQuests().getAQuest(data.getFestival().getQuest());
        ContestEntries entries = quest.getEntries();
        World world = ageable.worldObj;
        ContestEntry third = entries.getEntry(THIRD);
        ContestEntry second = entries.getEntry(SECOND);
        ContestEntry first = entries.getEntry(FIRST);
        return StringEscapeUtils.unescapeJava(I18n.translateToLocalFormatted(unlocalised, third.getOwnerName(), third.getEntityName(world),
                second.getOwnerName(), second.getEntityName(world),
                first.getOwnerName(), first.getEntityName(world)));
    }
}

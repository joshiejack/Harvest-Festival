package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.quests.base.QuestAnimalContest;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.quests.town.festivals.Place.*;

public class ContestWinningScript extends Script {
    private Festival festival;

    public ContestWinningScript(Festival festival, NPC npc) {
        super(new ResourceLocation(MODID, festival.getResource().getResourceDomain() + "_winner"));
        this.festival = festival;
        this.setNPC(npc);
    }

    @Override
    public String getLocalized(EntityAgeable ageable, NPC npc) {
        QuestAnimalContest quest = TownHelper.getClosestTownToEntity(ageable, false).getQuests().getAQuest(festival.getQuest());
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

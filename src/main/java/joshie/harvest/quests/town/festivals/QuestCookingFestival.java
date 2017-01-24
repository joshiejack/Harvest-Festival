package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.selection.FestivalSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@HFQuest("festival.cooking")
public class QuestCookingFestival extends QuestFestival {
    private final Selection selection = new FestivalSelection("cooking");
    private final int QUESTION = 0;

    public QuestCookingFestival() {
        setNPCs(HFNPCs.GS_OWNER);
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        if (quest_stage <= QUESTION) {
            return selection;
        } else return null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (quest_stage == QUESTION) return "We are describing the contest to the player";
        else return "We have started the contest...";
    }
}

package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestFestivalTimed;
import joshie.harvest.quests.town.festivals.harvest.HarvestSelection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import javax.annotation.Nullable;

@HFQuest("festival.harvest")
public class QuestHarvestFestival extends QuestFestivalTimed {
    private final HarvestSelection selection;

    public QuestHarvestFestival() {
        setNPCs(HFNPCs.FLOWER_GIRL);
        selection = new HarvestSelection();
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return selection.getInserted() == null ? selection : null;
    }

    @Override
    protected boolean isCorrectTime(long time) {
        return time >= 8000L && time <= 20000L;
    }

    @Override
    @Nullable
    protected String getLocalizedScript(EntityPlayer player, NPC npc) {
        return selection.getLocalizedScript(player, npc);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if (selection.getInserted() != null && !selection.hasRemoved()) {
            player.setHeldItem(EnumHand.MAIN_HAND, null);
            complete(player); //Finish the festival
        }
    }

    //Execute the festival
    public void execute(EntityPlayer player, NPCEntity entity) {

    }
}

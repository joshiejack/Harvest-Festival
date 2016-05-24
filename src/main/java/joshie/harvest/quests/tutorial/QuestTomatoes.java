package joshie.harvest.quests.tutorial;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.HashSet;

import static joshie.harvest.core.helpers.QuestHelper.*;

public class QuestTomatoes extends Quest {
    @Override
    public boolean canStart(EntityPlayer player, HashSet<IQuest> active, HashSet<IQuest> finished) {
        if (!super.canStart(player, active, finished)) return false;
        else {
            return true;
        }
    }

    @Override
    public INPC[] getNPCs() {
        return new INPC[]{HFNPCs.GODDESS};
    }

    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        if (quest_stage == 0) {
            increaseStage(player);
            return getLocalized("start");
        } else if (player.getActiveItemStack() != null) {
            ItemStack held = player.getActiveItemStack();
            if (held.stackSize >= 10) {
                if (HFCrops.TOMATO.matches(held)) {
                    takeHeldStack(player, 10);
                    completeQuest(player, this);
                    return getLocalized("finish");
                }
            }

            return getLocalized("wrong");
        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardGold(player, HFCrops.TOMATO.getSellValue(HFCrops.TOMATO.getCropStack()) * 15);
        rewardRelations(player, HFNPCs.GODDESS, 1000);
    }
}
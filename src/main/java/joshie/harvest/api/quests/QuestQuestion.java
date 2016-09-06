package joshie.harvest.api.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

/** This is a helper class that I use in my tutorials, to see if the quest is completed early
 *  At the start of the script I pretty much always do:
 *  if (isCompletedEarly) {
 *      complete(player);
 *      return "completed";
 *  }                               **/
public abstract class QuestQuestion extends Quest {
    protected final Selection selection;
    public boolean isCompletedEarly;

    public QuestQuestion(Selection selection) {
        this.selection = selection;
    }

    @Override
    public Selection getSelection(EntityPlayer player, INPC npc) {
        return quest_stage <= 0 ? selection : null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        isCompletedEarly = nbt.getBoolean("IsCompleted");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("IsCompleted", isCompletedEarly);
        return nbt;
    }

    public abstract static class QuestSelection<Q extends QuestQuestion> extends Selection<Q> {
        public QuestSelection(String title, String line1, String line2) {
            super(title, line1, line2);
        }

        public QuestSelection(String title, String line1, String line2, String line3) {
            super(title, line1, line2, line3);
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, QuestQuestion quest, int option) {
            if (option == 1) { //If it's our first time, start tutorials
                quest.increaseStage(player);
            } else { //If it's not then give the player the essentials to get started
                HFApi.quests.completeEarly(quest, player);
            }

            return Result.ALLOW;
        }
    }
}

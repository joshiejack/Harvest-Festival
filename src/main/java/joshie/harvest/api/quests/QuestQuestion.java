package joshie.harvest.api.quests;

import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

/** This is a helper class that I use in my tutorials, to see if the quest is completed early
 *  At the start of the script I pretty much always do:
 *  if (isCompletedEarly()) {
 *      complete(player);
 *      return getLocalized("completed");
 *  }                               **/
public abstract class QuestQuestion extends Quest {
    protected final QuestSelection selection;

    public QuestQuestion(QuestSelection selection) {
        this.selection = selection;
    }

    public boolean isCompletedEarly() {
        return selection.finishedEarly;
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return getLocalizedScript(player, npc);
    }

    protected abstract String getLocalizedScript(EntityPlayer player, NPC npc);

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (isCompletedEarly()) {
            complete(player);
        } else onChatClosed(player, npc);
    }

    protected abstract void onChatClosed(EntityPlayer player, NPC npc);

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return quest_stage <= 0 ? selection : null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        selection.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return selection.writeToNBT(super.writeToNBT(nbt));
    }

    public abstract static class QuestSelection<Q extends QuestQuestion> extends Selection<Q> {
        protected boolean finishedEarly;

        public QuestSelection(String title, String line1, String line2) {
            super(title, line1, line2);
        }

        public QuestSelection(String title, String line1, String line2, String line3) {
            super(title, line1, line2, line3);
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, NPC npc, QuestQuestion quest, int option) {
            if (option == 1) { //If it's our first time, start tutorials
                quest.quest_stage++;
            } else { //If it's not then give the player the essentials to get started
                finishedEarly = true;
            }

            return Result.ALLOW;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            finishedEarly = tag.getBoolean("FinishedEarly");
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setBoolean("FinishedEarly", finishedEarly);
            return super.writeToNBT(tag);
        }
    }
}

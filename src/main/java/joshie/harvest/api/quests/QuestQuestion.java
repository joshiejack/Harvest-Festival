package joshie.harvest.api.quests;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
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
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return getLocalizedScript(player, entity.getNPC());
    }

    protected abstract String getLocalizedScript(EntityPlayer player, NPC npc);

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (isCompletedEarly()) {
            complete(player);
        } else onChatClosed(player, entity.getNPC());
    }

    protected abstract void onChatClosed(EntityPlayer player, NPC npc);

    @Override
    public Selection getSelection(EntityPlayer player, NPCEntity entity) {
        return quest_stage <= 0 && !isCompletedEarly() ? selection : null;
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
        boolean finishedEarly;

        public QuestSelection(String title, String line1, String line2) {
            super(title, line1, line2);
        }

        @Override
        public Result onSelected(EntityPlayer player, NPCEntity entity, QuestQuestion quest, int option) {
            if (option == 1) { //If it's our first time, start tutorials
                quest.quest_stage++;
            } else { //If it's not then give the player the essentials to get started
                finishedEarly = true;
            }

            return Result.ALLOW;
        }

        public void readFromNBT(NBTTagCompound tag) {
            finishedEarly = tag.getBoolean("FinishedEarly");
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setBoolean("FinishedEarly", finishedEarly);
            return tag;
        }
    }
}
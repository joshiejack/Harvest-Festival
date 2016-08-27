package joshie.harvest.quests;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class QuestQuestion extends Quest {
    private final Selection selection;
    protected int selected;

    public QuestQuestion(Selection selection) {
        this.selection = selection;
    }

    @Override
    public Selection getSelection(INPC npc) {
        return quest_stage <= 0 ? selection : null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        selected = nbt.getByte("Selected");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("Selected", (byte) selected);
        return nbt;
    }

    public abstract static class QuestSelection<Q extends QuestQuestion> extends AbstractSelection<Q> {
        public QuestSelection(String title, String line1, String line2) {
            super(title, line1, line2);
        }

        public QuestSelection(String title, String line1, String line2, String line3) {
            super(title, line1, line2, line3);
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, Q quest, int option) {
            quest.selected = option;
            quest.increaseStage(player);
            return Result.ALLOW;
        }
    }
}

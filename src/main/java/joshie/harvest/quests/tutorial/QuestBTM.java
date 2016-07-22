package joshie.harvest.quests.tutorial;

import joshie.harvest.api.HFRegister;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.AbstractSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.npc.HFNPCs.CAFE_OWNER;
import static joshie.harvest.quests.QuestHelper.rewardItem;

@HFRegister(data = "test")
public class QuestBTM extends Quest {
    private static final Selection selection = new TeamSelection();
    private int selected;

    public QuestBTM() {
        setNPCs(CAFE_OWNER);
    }

    @Override
    public Selection getSelection() {
        return quest_stage <= 0 ? selection : null;
    }

    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        if (previous == 0) {
            if (selected == 1) rewardItem(player, new ItemStack(Blocks.REDSTONE_BLOCK, 4, 0));
            else if (selected == 2) rewardItem(player, new ItemStack(Blocks.GOLD_BLOCK, 4, 0));
            else if (selected == 3) rewardItem(player, new ItemStack(Blocks.LAPIS_BLOCK, 4, 0));
        }
    }

    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0) return "start";
        else if (quest_stage == 1) {
            complete(player);
            return "done";
        } else return null;
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

    private static class TeamSelection extends AbstractSelection<QuestBTM> {
        public TeamSelection() {
            super("test.question", "test.red", "test.yellow", "test.blue");
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, QuestBTM quest, int option) {
            quest.selected = option;
            quest.increaseStage(player);
            return Result.ALLOW;
        }
    };
}

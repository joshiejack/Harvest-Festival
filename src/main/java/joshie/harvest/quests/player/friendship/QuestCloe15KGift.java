package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.item.ItemNPCTool;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

@HFQuest("friendship.cloe.gift")
public class QuestCloe15KGift extends QuestFriendship {
    public QuestCloe15KGift() {
        super(HFNPCs.DAUGHTER_ADULT, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.CLOE_10K);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected ItemStack getRewardStack() {
        ItemStack stack = HFNPCs.TOOLS.getStackFromEnum(NPCTool.GIFT);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setBoolean(ItemNPCTool.SPECIAL, true);
        return stack;
    }
}

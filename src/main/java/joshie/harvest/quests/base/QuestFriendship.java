package joshie.harvest.quests.base;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public abstract class QuestFriendship extends Quest {
    protected final int relationship;

    public QuestFriendship(NPC npc, int relationship) {
        setNPCs(npc);
        this.relationship = relationship;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return super.isNPCUsed(player, entity) && HFApi.player.getRelationsForPlayer(player).getRelationship(entity.getNPC()) >= relationship;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return getLocalized("text");
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean isSneaking) {
        complete(player);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        List<ItemStack> stacks = getRewardStacks(player);
        if (stacks != null) {
            for (ItemStack stack: stacks) {
                rewardItem(player, stack);
            }
        }
    }

    protected List<ItemStack> getRewardStacks(EntityPlayer player) {
        return getRewardStack() != null ? Lists.newArrayList(getRewardStack()) : null;
    }

    protected ItemStack getRewardStack() { return null; }
}

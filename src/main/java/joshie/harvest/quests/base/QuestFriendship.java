package joshie.harvest.quests.base;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class QuestFriendship extends Quest {
    protected final int relationship;

    public QuestFriendship(NPC npc, int relationship) {
        setNPCs(npc);
        this.relationship = relationship;
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        return super.isNPCUsed(player, npc) && HFApi.player.getRelationsForPlayer(player).getRelationship(npc) >= relationship;
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
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return getLocalized("text");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean isSneaking) {
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
        return getRewardStack() != null ? Collections.singletonList(getRewardStack()) : null;
    }

    protected ItemStack getRewardStack() { return null; }
}

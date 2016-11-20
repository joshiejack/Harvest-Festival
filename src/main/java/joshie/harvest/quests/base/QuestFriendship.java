package joshie.harvest.quests.base;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
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

    public QuestFriendship(INPC npc, int relationship) {
        setNPCs(npc);
        this.relationship = relationship;
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
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getUUID()) >= relationship) {
            return getLocalized("text");
        } else return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean isSneaking) {
        if (HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getUUID()) >= relationship) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        List<ItemStack> stacks = getRewardStacks();
        if (stacks != null) {
            for (ItemStack stack: stacks) {
                rewardItem(player, stack);
            }
        }
    }

    protected List<ItemStack> getRewardStacks() {
        return getRewardStack() != null ? Collections.singletonList(getRewardStack()) : null;
    }

    protected ItemStack getRewardStack() { return null; }
}

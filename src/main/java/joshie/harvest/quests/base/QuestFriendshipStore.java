package joshie.harvest.quests.base;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class QuestFriendshipStore extends QuestFriendship {
    public QuestFriendshipStore(INPC npc, int relationship) {
        super(npc, relationship);
    }

    protected abstract Quest getQuest();

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getUUID()) >= relationship) {
            return HFApi.quests.getCurrentQuests(player).contains(getQuest()) ? getLocalized("item"): getLocalized("text");
        } else return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (HFApi.quests.getCurrentQuests(player).contains(getQuest())) {
            List<ItemStack> stacks = getRewardStacks(player);
            if (stacks != null) {
                for (ItemStack stack : stacks) {
                    rewardItem(player, stack);
                }
            }
        } else HFApi.quests.completeQuestConditionally(getQuest(), player);
    }
}

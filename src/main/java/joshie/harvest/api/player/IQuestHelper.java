package joshie.harvest.api.player;

import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IQuestHelper {
    /** Increases the stage of the quest
     *  @param quest    the quest to sync
     *  @param player   the player syncing to*/
    void increaseStage(Quest quest, EntityPlayer player);

    /** Completes a quest
     *  @param quest    the quest to mark as completed
     *  @param player   the player to complete the quest for**/
    void completeQuest(Quest quest, EntityPlayer player);

    /** Takes an item from a players held items,
     *  CALL ONE SIDE ONLY
     *  @param player   the player to take from
     *  @param amount   the amount to take from the stack*/
    void takeHeldStack(EntityPlayer player, int amount);

    /** Rewards a player with an item, if called on the client side,
     *  the item will be validated against the quest to ensure it can be rewarded
     *  @param quest    the quest we're taking from, so it can be validated
     *  @param player   the player
     *  @param stack    the item you want to give the player **/
    void rewardItem(Quest quest, EntityPlayer player, ItemStack stack);

    /** Rewards a player with gold, SERVER SIDE ONLY
     *  If you try to call it on the client, an exception will be thrown
     *  @param player   the player
     *  @param gold     how much gold to give**/
    void rewardGold(EntityPlayer player, long gold);

    /** Rewards the player with an entity, if called client side,
     *  This will be validated in the quest on the server
     * @param quest     the quest
     * @param player    the player
     * @param entity    the entity */
    void rewardEntity(Quest quest, EntityPlayer player, String entity);
}

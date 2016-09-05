package joshie.harvest.quests.tutorial;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.generic.OreDictionaryHelper;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.SearchType.FLOWER;
import static joshie.harvest.core.lib.HFQuests.TUTORIAL_INTRO;
import static joshie.harvest.npc.HFNPCs.*;

@HFQuest("tutorial.carpenter")
public class QuestCarpenter extends Quest {
    private boolean attempted = false;

    public QuestCarpenter() {
        setNPCs(GODDESS, BUILDER, SEED_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_INTRO);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0 && npc == HFNPCs.GODDESS) {
            //Goddess Talks about welcoming you back, She tells you that she's ready to help you get started with restoring the town
            //She explains that she heard of a guy called yulif and his sister jade that are happy to be the first to return
            //Jade has a love of nature so it's all too exciting, while yulife is a carpenter, so he's able to build homes
            //With this information in hand, the goddess lets you know that he needs some logs to build his house
            //She requests that you go and collect 64 logs for her, and then she'll have something for you in return
            increaseStage(player);
            return "welcome";
        } else if (quest_stage == 1 && npc == HFNPCs.GODDESS) {
            ItemStack held = player.getHeldItemMainhand();
            if (held != null) {
                if (held.stackSize >= 64 && OreDictionaryHelper.isLogs(held)) {
                    takeHeldStack(player, 64);
                    //Goddess Tells the player thank you for the logs
                    //She then gifts the player a blueprint tells the player that this is a blueprint
                    //You can use them to instruct the carpenter to build a new building
                    //It will display a ghost image of the building, helping you plan where to place it
                    //Simply right click this, and all of a sudden a builder will appear near the site
                    //He will start building, she also mentions that when you have multiple blueprints
                    //Right clicking will add them to a queue, and the builder will get around to the building eventually
                    //After explaining what it does, she sets you a task to build the carpenter, she also reminds you that Jade
                    //Will move in with yulif and she can often be found upstairs in the house, she requests that you deliver some sort of flower
                    //To jaded, and tells you that she carries around a bunch of seeds, but she is always on the lookout for flowers instead
                    //Normally she would ask for 10 flowers, but for a one off deal she is doing one flower for some seeds
                    increaseStage(player);
                    return "thanks.build";
                }
            }

            //The goddess reminds you that she wants you to give her 64 logs
            return "reminder.wood";
        } else if (quest_stage == 2 && npc == HFNPCs.GODDESS) {
            //The Goddess reminds the player that she has asked you to deliver a flower to jaded, and to do so
            //You must get the carpenter house built, she says that if you lost the blueprint, then bring the goddess
            //Another 64 logs of wood, and she will happily give you a blueprint again
            ItemStack held = player.getHeldItemMainhand();
            if (attempted && held != null && held.stackSize >= 64 && OreDictionaryHelper.isLogs(held)) {
                takeHeldStack(player, 64);
                rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
                return "reminder.give";
            } else {
                attempted = true;
                return "reminder.carpenter";
            }
        } else if (quest_stage == 2 && npc == HFNPCs.SEED_OWNER) {
            ItemStack held = player.getHeldItemMainhand();
            if (held != null) {
                if (InventoryHelper.SPECIAL.matches(held, FLOWER)) {
                    //Jade thanks you for the flowers, she then proceeds
                    //She then informs you that the goddess would like to see you again
                    //She has a reward, She says to come back to see her after you have
                    //revisited the goddess, as she has something to show you
                    InventoryHelper.takeItems(player, FLOWER, 1);
                    increaseStage(player);
                    return "thanks.flowers";
                }
            }

            //Jade tells you that it is nice to meet you, and that she's looking for some kind of flower
            return "reminder.flowers";
        } else if (quest_stage == 3 && npc == HFNPCs.GODDESS) {
            complete(player);
            //Goddess thanks the player for helping out get the town started, She explains that she understands that you'll need something
            //to get started, so she gives you 1000 gold
            return "thanks.finish";
        }

        return null;
    }

    @Override
    public boolean canReward(ItemStack stack) {
        return stack.isItemEqual(HFBuildings.CARPENTER.getBlueprint());
    }

    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        if (previous == 1) { //Give the player a carpenter blueprint to place
            rewardItem(player, HFBuildings.CARPENTER.getBlueprint());
        }
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardGold(player, 1000);
    }
}
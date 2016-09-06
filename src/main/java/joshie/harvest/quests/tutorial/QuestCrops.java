package joshie.harvest.quests.tutorial;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.quests.TutorialSelection;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.api.core.ITiered.ToolTier.BASIC;
import static joshie.harvest.core.helpers.InventoryHelper.SearchType.FLOWER;
import static joshie.harvest.core.lib.HFQuests.TUTORIAL_CARPENTER;
import static joshie.harvest.npc.HFNPCs.SEED_OWNER;

@HFQuest("tutorial.crops")
public class QuestCrops extends QuestQuestion {
    private boolean attempted;

    public QuestCrops() {
        super(new TutorialSelection("crops"));
        setNPCs(SEED_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_CARPENTER);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isCompletedEarly) {
            complete(player);
            return "completed";
        } else if (quest_stage == 0) {
            //Jade says hello to the player, and asks them if they know how to farm
            return "intro";
        } else if (quest_stage == 1) {
            //Jade explains farming, and how you need to hoe the ground, then plant seeds, and then water them everyday
            //She asks that you take this hoe, this watering can and three turnip seeds, and return to her when you have 9 turnips
            //She also explains that you right click the ground to hoe it
            //She explains that you must fill the watering can with water before you can use it
            //She also explains that if you wish to speed up growth time, you can sleep, crops will grow as the day ticks over
            //She also explains that crops will only grow in very specific seasons
            increaseStage(player);
            return "start";
        } else if (quest_stage == 2) {
            ItemStack held = player.getHeldItemMainhand();
            if (attempted && held != null) {
                if (held.stackSize >= 10 && InventoryHelper.SPECIAL.matches(held, SearchType.FLOWER)) {
                    rewardItem(player, HFCrops.TURNIP.getSeedStack());
                    takeHeldStack(player, 10);
                    //Jade thanks the player for the flowers, and gives them turnip seeds
                    return "thanks.flowers";
                } else if (InventoryHelper.SPECIAL.matches(held, SearchType.HOE)) {
                    rewardItem(player, HFTools.HOE.getStack(BASIC));
                    takeHeldStack(player, 1);
                    //Jade thanks the player for the hoe and gives them a hf hoe
                    return "thanks.hoe";
                } else if (InventoryHelper.SPECIAL.matches(held, SearchType.BUCKET)) {
                    rewardItem(player, HFTools.WATERING_CAN.getStack(BASIC));
                    takeHeldStack(player, 1);
                    //Jade thanks the player for the bucket and gives them a watering can
                    return "thanks.bucket";
                } else if (held.stackSize >= 9 && InventoryHelper.ITEM_STACK.matches(held, HFCrops.TURNIP.getCropStack())) {
                    //Jade thanks the player for the turnips, She tells the player you know what
                    //Thanks but you can keep them, She tells the player she doesn't want you to have wasted your time
                    //to make her happy, she then informs you that you can sell your crops for money
                    //To do this you just need to get a shipping bin, you can right click to place the crops inside
                    //Or you can use a hopper if you don't feel like spending all day placing them in
                    //She explains that you can purchase shipping bins directly from yulif
                    //She also informs the player that they can come visit her anytime
                    //She will gladly give them a bag of seeds for 10 flowers
                    //She however also explains that her variety is limited, and suggests that you build a supermarket
                    //She also informs the player that the harvest goddess has heard of your great work
                    //And that she would really like to hear from you, in fact she would love to see a turnip!
                    complete(player);
                    return "complete";
                }
            }

            //Jade reminds the player that to continue she is after 9 turnips
            //She also tells the player that if they've lost their hoe, then they can bring her
            //a hoe from the vanilla world, and she will gladly trade for it
            //She also tells the player she will trade a watering can for a bucket
            //And that she will give them a bag of turnips for 10 flowers
            attempted = true;
            return "reminder.turnips";
        }

        return null;
    }

    @Override
    public boolean canReward(ItemStack stack) {
        return InventoryHelper.SPECIAL.matches(stack, FLOWER) || stack.isItemEqual(HFTools.HOE.getStack(BASIC)) || stack.isItemEqual(HFTools.WATERING_CAN.getStack(BASIC)) || stack.isItemEqual(HFCrops.TURNIP.getSeedStack());
    }

    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {
        if (previous == 0) { //Give the player the basic tools to get started with farming
            rewardItem(player, HFTools.HOE.getStack(BASIC));
            rewardItem(player, HFTools.WATERING_CAN.getStack(BASIC));
            Item seeds = HFCrops.TURNIP.getSeedStack().getItem();
            int damage = HFCrops.TURNIP.getSeedStack().getItemDamage();
            rewardItem(player, new ItemStack(seeds, 3, damage));
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (quest_stage == 0) { //We were not new
            rewardItem(player, HFTools.HOE.getStack(BASIC));
            rewardItem(player, HFTools.WATERING_CAN.getStack(BASIC));
            Item seeds = HFCrops.TURNIP.getSeedStack().getItem();
            int damage = HFCrops.TURNIP.getSeedStack().getItemDamage();
            rewardItem(player, new ItemStack(seeds, 3, damage));
        }
    }
}
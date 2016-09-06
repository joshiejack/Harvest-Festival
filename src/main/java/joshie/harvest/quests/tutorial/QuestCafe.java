package joshie.harvest.quests.tutorial;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.block.BlockCookware.Cookware;
import joshie.harvest.cooking.item.ItemUtensil.Utensil;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.quests.TutorialSelection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.core.lib.HFQuests.TUTORIAL_UPGRADING;
import static joshie.harvest.npc.HFNPCs.CAFE_OWNER;

@HFQuest("tutorial.cafe")
public class QuestCafe extends QuestQuestion {
    public QuestCafe() {
        super(new TutorialSelection("cafe"));
        setNPCs(CAFE_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_UPGRADING);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isCompletedEarly) {
            complete(player);
            return "completed";
        } else if (quest_stage == 0) {
            //Liara tells the player, welcome to the cafe, she tells them that she is an expert on cooking
            //She asks them if they have ever cooked before or if they know how?
            return "welcome";
        } else if (quest_stage == 1) {
            //Liara explains that to cook you will need a utensil
            //You will also need to know a recipe, she tells you that you can look up recipes
            //In the cookbook, she explains that the book will tell you
            //If you have all ingredients in your inventory or not
            //She explains that with the recipe book in hand you can open it
            //Then click the cook button, this will put the items in to the inventory
            //Of a valid nearby utensil, it will then cook it, and eventually it will be cooked
            //She explains that meals can often sell for a lot more than their raw ingredients
            //She also explains that she will sell cookware at the weekends
            //She also explains that the recipes listed in the book are the basic
            //And that you should try adding different ingredients to make better versions
            //She thanks you for your time, and being the first customer and gives the rewards
            complete(player);
            return "explain";
        }

        return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCooking.UTENSILS.getStackFromEnum(Utensil.KNIFE));
        rewardItem(player, HFCooking.COOKWARE.getStackFromEnum(Cookware.COUNTER));
        rewardItem(player, new ItemStack(HFCooking.COOKBOOK));
        rewardItem(player, CookingHelper.getRecipe("turnip_pickled"));
    }
}
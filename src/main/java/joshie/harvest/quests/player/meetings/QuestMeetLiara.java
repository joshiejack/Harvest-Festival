package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.block.BlockCookware.Cookware;
import joshie.harvest.cooking.item.ItemUtensil.Utensil;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.npc.HFNPCs.CAFE_OWNER;
import static joshie.harvest.quests.Quests.JENNI_MEET;

@HFQuest("tutorial.cafe")
public class QuestMeetLiara extends QuestQuestion {
    private static final int WELCOME = 0;
    private static final int TUTORIAL = 1;
    public QuestMeetLiara() {
        super(new TutorialSelection("cafe"));
        setNPCs(CAFE_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JENNI_MEET);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (!TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.CAFE)) return null;
        if (isCompletedEarly) {
            return getLocalized("completed");
        } else if (quest_stage == WELCOME) {
            //Liara tells the player, welcome to the cafe, she tells them that she is an expert on cooking
            //She asks them if they have ever cooked before or if they know how?
            return getLocalized("welcome");
        } else if (quest_stage == TUTORIAL) {
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
            return getLocalized("explain");
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (isCompletedEarly || quest_stage == TUTORIAL) {
            if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.CAFE)) {
                complete(player);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.quests.completeQuestConditionally(Quests.BUILDING_CAFE, player);
        rewardItem(player, HFCooking.UTENSILS.getStackFromEnum(Utensil.KNIFE));
        rewardItem(player, HFCooking.COOKWARE.getStackFromEnum(Cookware.COUNTER));
        rewardItem(player, new ItemStack(HFCooking.COOKBOOK));
        rewardItem(player, CookingHelper.getRecipe("turnip_pickled"));
        HFApi.player.getTrackingForPlayer(player).addAsObtained(HFCooking.COOKWARE.getStackFromEnum(Cookware.COUNTER));
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.RECIPES);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.RECIPE_BOOK);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.KITCHEN_COUNTER);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.COOKING);
    }
}
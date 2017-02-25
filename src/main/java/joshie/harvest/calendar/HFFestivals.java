package joshie.harvest.calendar;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.block.BlockCookware.Cookware;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.knowledge.letter.LetterFestival;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.api.knowledge.Category.TOWNSHIP;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.LoadOrder.HFFESTIVALS;
import static joshie.harvest.knowledge.HFNotes.registerNote;

@HFLoader(priority = HFFESTIVALS)
@SuppressWarnings("unchecked")
public class HFFestivals {
    private static Map<Festival, Season> TEMP_REGISTRY = new HashMap<>();
    public static final Festival NEW_YEARS = registerFestival("new_years", 1, SPRING).setLength(1);
    public static final Festival COOKING_CONTEST = registerFestival("cooking", 22, SPRING).setRequirement(HFBuildings.CAFE);
    public static final Festival CHICKEN_FESTIVAL = registerFestival("chicken", 7, SUMMER).setRequirement(HFBuildings.POULTRY_FARM);
    public static final Festival COW_FESTIVAL = registerFestival("cow", 20, SUMMER).setRequirement(HFBuildings.BARN);
    public static final Festival HARVEST_FESTIVAL = registerFestival("harvest", 9, AUTUMN).setLength(1);
    public static final Festival SHEEP_FESTIVAL = registerFestival("sheep", 21, AUTUMN).setRequirement(HFBuildings.BARN);
    public static final Festival STARRY_NIGHT = registerFestival("starry_night", 24, WINTER).setLength(1);
    public static final Festival NEW_YEARS_EVE = registerFestival("new_years_eve", 30, WINTER).setLength(1);


    public static void init() {
        for (Festival festival: TEMP_REGISTRY.keySet()) {
            String name = "festival." + festival.getResource().getResourcePath().replace("_", ".");
            festival.setQuest(QuestHelper.getQuest(name)).setNote(registerNote(TOWNSHIP, name)).setLetter(new LetterFestival(festival, TEMP_REGISTRY.get(festival), festival.getResource()));
        }

        NEW_YEARS.setIcon(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.RICEBALL));
        COOKING_CONTEST.setIcon(HFCooking.COOKWARE.getStackFromEnum(Cookware.FRYING_PAN));
        CHICKEN_FESTIVAL.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.CHICKEN));
        COW_FESTIVAL.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.COW));
        HARVEST_FESTIVAL.setIcon(new ItemStack(Items.CAULDRON));
        SHEEP_FESTIVAL.setIcon(HFAnimals.ANIMAL.getStackFromEnum(Spawner.SHEEP));
        STARRY_NIGHT.setIcon(new ItemStack(Blocks.SAPLING, 1, 1));
        NEW_YEARS_EVE.setIcon(HFCooking.MEAL.getStackFromEnum(Meal.NOODLES));
        TEMP_REGISTRY = null; //save memory
    }

    private static Festival registerFestival(String name, int day, Season season) {
        ResourceLocation resource = new ResourceLocation(MODID, name);
        Festival festival = new Festival(resource);
        HFApi.calendar.registerFestival(festival, day, season);
        TEMP_REGISTRY.put(festival, season);
        return festival;
    }
}

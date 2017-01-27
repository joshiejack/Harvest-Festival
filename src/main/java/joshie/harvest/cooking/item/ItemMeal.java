package joshie.harvest.cooking.item;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.cooking.recipe.RecipeHF;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static joshie.harvest.api.cooking.Utensil.*;
import static joshie.harvest.cooking.recipe.RecipeBuilder.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;
import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemMeal extends ItemHFFoodEnum<ItemMeal, Meal> {
    private static final EnumMap<Meal, Recipe> MEAL_TO_RECIPE = new EnumMap<>(Meal.class);
    public ItemMeal() {
        super(HFTab.COOKING, Meal.class);
    }

    public enum Meal implements IStringSerializable {
        PANCAKE_SAVOURY, FRIES_FRENCH, POPCORN, CORNFLAKES, EGGPLANT_HAPPY, EGG_SCRAMBLED(true), OMELET, OMELET_RICE,
        TOAST_FRENCH, DOUGHNUT, FISH_GRILLED, PANCAKE, POTSTICKER, RISOTTO, JUICE_PINEAPPLE, JUICE_TOMATO, MILK_STRAWBERRY,
        JUICE_VEGETABLE, LATTE_VEGETABLE, KETCHUP(true), BUTTER(true), FISHSTICKS, TURNIP_PICKLED, CUCUMBER_PICKLED, SALAD, SANDWICH,
        SUSHI, SASHIMI(true), SASHIMI_CHIRASHI, MILK_HOT, CHOCOLATE_HOT, EGG_BOILED, SPINACH_BOILED, POTATO_CANDIED, DUMPLINGS,
        NOODLES, SOUP_RICE, PORRIDGE, EGG_OVERRICE, STEW, STEW_PUMPKIN, STEW_FISH, CORN_BAKED, RICEBALLS_TOASTED,
        TOAST, DINNERROLL, DORIA, COOKIES(true), COOKIES_CHOCOLATE, CAKE_CHOCOLATE,
        //0.6+ Meals
        BURNT_COUNTER(COUNTER), BURNT_FRYING_PAN(FRYING_PAN), BURNT_MIXER(MIXER), BURNT_OVEN(OVEN), BURNT_POT(POT), //Burnt as separate metadata
        STIR_FRY, RICE_FRIED, SOUFFLE_APPLE, BREAD_CURRY, NOODLES_THICK_FRIED, TEMPURA, CURRY_DRY,
        JUICE_GRAPE, JUICE_PEACH, JUICE_BANANA, JUICE_ORANGE, JUICE_APPLE, JUICE_FRUIT, LATTE_FRUIT, JUICE_MIX, LATTE_MIX,
        SANDWICH_FRUIT, RICE_BAMBOO, RICE_MATSUTAKE, RICE_MUSHROOM, BREAD_RAISIN, ICE_CREAM,
        JAM_STRAWBERRY, JAM_APPLE, JAM_GRAPE, MARMALADE, NOODLES_TEMPURA, RICE_TEMPURA,
        BUN_JAM, SWEET_POTATOES, CAKE, PIE_APPLE, SALAD_HERB, SOUP_HERB, SANDWICH_HERB;

        private final boolean hasAltTexture;
        private final Utensil utensil;

        Meal() {
            this.hasAltTexture = false;
            this.utensil = null;
        }

        Meal(boolean hasAltTexture) {
            this.hasAltTexture = hasAltTexture;
            this.utensil = null;
        }

        Meal (Utensil utensil) {
            this.hasAltTexture = false;
            this.utensil = utensil;
        }

        public boolean hasAltTexture() {
            return hasAltTexture;
        }

        public Utensil getUtensil() {
            return utensil;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemStack getRandomMeal(Random rand) {
        boolean first = rand.nextBoolean();
        if (first) return getCreativeStack(this, Meal.values()[rand.nextInt(50)]);
        else return getCreativeStack(this, Meal.values()[55 + rand.nextInt(35)]);
    }

    private Recipe getRecipeFromMeal(Meal meal) {
        if (MEAL_TO_RECIPE.size() == 0) {
            for (Meal ameal: Meal.values()) {
                MEAL_TO_RECIPE.put(ameal, Recipe.REGISTRY.getValue(new ResourceLocation(MODID, ameal.getName())));
            }
        }

        return MEAL_TO_RECIPE.get(meal);
    }

    public ItemStack getStackFromRecipe(RecipeHF recipeHF) {
        Meal meal = Meal.valueOf(recipeHF.getRegistryName().getResourcePath().toUpperCase(Locale.ENGLISH));
        return new ItemStack(this, 1, meal.ordinal());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Meal meal = getEnumFromStack(stack);
        if (meal.getUtensil() != null) return DARK_GRAY + TextHelper.localize(meal.getUtensil().getUnlocalizedName());
        Recipe impl = getRecipeFromMeal(meal);
        return impl != null ? impl.getDisplayName() : "Corrupted Meal";
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger(FOOD_LEVEL) : 0;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getFloat(SATURATION_LEVEL) : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (debug) {
            if (stack.hasTagCompound()) {
                list.add(TextHelper.translate("meal.hunger") + " : " + stack.getTagCompound().getInteger(FOOD_LEVEL));
                list.add(TextHelper.translate("meal.sat") + " : " + stack.getTagCompound().getFloat(SATURATION_LEVEL));
                list.add(TextHelper.translate("meal.sell") + " : " + stack.getTagCompound().getLong(SELL_VALUE));
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (stack.hasTagCompound() && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            player.getFoodStats().addStats(this, stack);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            Recipe recipe = getRecipeFromMeal(getEnumFromStack(stack));
            return recipe == null ? 32 : recipe.getEatTimer();
        } else return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.hasTagCompound()) {
            Recipe recipe = getRecipeFromMeal(getEnumFromStack(stack));
            return recipe == null ? EnumAction.NONE : recipe.getAction();
        } else return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(false)) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public ItemStack getCreativeStack(Item item, Meal meal) {
        Recipe recipe = getRecipeFromMeal(meal);
        if (recipe != null) {
            ArrayList<IngredientStack> stacks = new ArrayList<>();
            stacks.addAll(recipe.getRequired());
            //if (recipe.getOptional().size() > 0) stacks.addAll(recipe.getOptional());
            ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
            stack.stackSize = 1;
            return stack;
        }

        return null;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {}
}
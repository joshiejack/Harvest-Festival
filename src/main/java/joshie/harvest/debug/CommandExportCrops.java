package joshie.harvest.debug;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.StateHandlerBasic;
import joshie.harvest.api.crops.StateHandlerBlock;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.handlers.state.StateHandlerGrass;
import joshie.harvest.crops.handlers.state.StateHandlerPumpkin;
import joshie.harvest.crops.handlers.state.StateHandlerWatermelon;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.*;

import static joshie.harvest.crops.HFCrops.*;

@HFDebugCommand
@SuppressWarnings("all")
public class CommandExportCrops extends CommandExportHeld {
    private static final Map<Crop, String> DESCRIPTIONS = new HashMap<>();
    private static final Map<Crop, int[]> OVERRIDES = new HashMap<>();
    static {
        DESCRIPTIONS.put(CUCUMBER, "Cucumbers are regrowable crops that are unlocked for purchase after a town has been around for one year.");
        DESCRIPTIONS.put(POTATO, "Potatoes grow slowly but are more valuable. They can be risky as there's a 1/50 chance of them producing a [[Poisonous Potato]] which are only worth {{gold|1}}. They do however also have a 1/20 chance of producing two potatoes.");
        DESCRIPTIONS.put(TURNIP, "The Turnip is normally the first crop you'll get your hands on. They grow quickly and are great for expanding your farm, if you want to grow more crops vs make more profit. You'll get a few free bags when you complete the [[Crops Tutorial]] from [[Jade]]. Jade will also give you access to the seeds until you have built a [[General Store]].");
        DESCRIPTIONS.put(CABBAGE, "Cabbage is a slow growing crop however they sell for a pretty penny! You can purchase them once you have shipped 1000 [[Spring]] crops.");
        DESCRIPTIONS.put(ONION, "Onions are the cheapest available summer crop. They don't regrow but they're a good starter crop.");
        DESCRIPTIONS.put(TOMATO, "Tomatoes are a regrowable crop. They will become available for purchase in year two.");
        DESCRIPTIONS.put(PUMPKIN, "Pumpkins take quite a while to grow however they have a great sell price so can make a decent profit.");
        DESCRIPTIONS.put(PINEAPPLE, "Pineapples take a very long time to grow. But they sell for quite a decent amount of money. You should be able to get two harvests out of them if you plant at the beginning of the season. They are unlocked by shipping 1000 [[Summer]] crops.");
        DESCRIPTIONS.put(WATERMELON, "Watermelons only grow once. While the individual melon may not sell for much. You can get up to nine per harvest if you make sure to use silk touch! You can start planting melons once a town has the following buildings: [[Tackle Shop]], [[Park]], [[Blacksmith]] and the [[Cafe]]. ");
        DESCRIPTIONS.put(CORN, "Corn is a regrowable crop that takes a while to get started. But once you get it started it will start raking in the cash. You can purchase corn from year three and on.");
        DESCRIPTIONS.put(EGGPLANT, "Eggplants are a regrowable crop. You will be able to purchase them from year two.");
        DESCRIPTIONS.put(SPINACH, "Spinach is a quick growing crop available right from the start");
        DESCRIPTIONS.put(CARROT, "Carrots while taking a little longer to grow than spinach also sell for a little more!");
        DESCRIPTIONS.put(SWEET_POTATO, "Sweet Potatoes are a very profitable crop. They regrow and only take a short six days to get started. You can purchase sweet potatoes when you have shipped 1000 [[Autumn]] crops.");
        DESCRIPTIONS.put(GREEN_PEPPER, "Green Peppers will continue producing after growing. They are purchasable from year three.");
        DESCRIPTIONS.put(BEETROOT, "Beetroots are the fastest growing crops and they sell for a decent amount too. In order to purchase them you will need to have built the [[Tackle Shop]], [[Park]], [[Blacksmith]] and the [[Cafe]].");
        DESCRIPTIONS.put(GRASS, "Grass is what you grow if you wish to not have to pay for [[Fodder]]. It will grow in all seasons except for [[Winter]] and requires a [[Sickle]] to be broken and takes 11 days to grow but can be harvested from day 6. It also does not need to be watered to grow");
        DESCRIPTIONS.put(WHEAT, "Wheat is the cheapest crop you can buy. It however takes a very long time to grow and requires a [[Sickle]] to be harvested. It can be grown in all season except for [[Winter]].");

        OVERRIDES.put(POTATO, new int[] { 2, 3, 6, 8 });
        OVERRIDES.put(CARROT, new int[] { 2, 3, 6, 8 });
    }

    @Override
    protected boolean isExportable(ItemStack stack) {
        return stack.getItem() == HFCrops.SEEDS;
    }

    private int[] getStagesFromCrops(Crop crop) {
        if (OVERRIDES.containsKey(crop)) return OVERRIDES.get(crop);
        if (crop.getStateHandler() instanceof StateHandlerBasic) {
            return ReflectionHelper.getPrivateValue(StateHandlerBasic.class, (StateHandlerBasic) crop.getStateHandler(), "values");
        } else if (crop.getStateHandler() instanceof StateHandlerPumpkin) {
            int[] var = new int[9];
            var[0] = 1;
            var[1] = 2;
            var[2] = 4;
            var[3] = 6;
            var[4] = 8;
            var[5] = 11;
            var[6] = 16;
            var[7] = 14;
            var[8] = 15;
            return var;
        } else if (crop.getStateHandler() instanceof StateHandlerWatermelon) {
            int[] var = new int[9];
            var[0] = 1;
            var[1] = 2;
            var[2] = 3;
            var[3] = 4;
            var[4] = 5;
            var[5] = 7;
            var[6] = 9;
            var[7] = 10;
            var[8] = 11;
            return var;
        } else if (crop.getStateHandler() instanceof StateHandlerGrass) {
            int[] var = new int[11];
            var[0] = 1;
            var[1] = 2;
            var[2] = 3;
            var[3] = 4;
            var[4] = 5;
            var[5] = 6;
            var[6] = 7;
            var[7] = 8;
            var[8] = 9;
            var[9] = 10;
            var[10] = 11;
            return var;
        } else if (crop.getStateHandler() instanceof StateHandlerBlock) {
            return ReflectionHelper.getPrivateValue(StateHandlerBlock.class, (StateHandlerBlock) crop.getStateHandler(), "values");
        }

        return null;
    }

    private String getRecipesForCrop(Crop crop) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();

        Ingredient theIngredient = CookingAPI.INSTANCE.getCookingComponents(crop.getCropStack(1));
        if (theIngredient != null) {
            IngredientStack istacks = new IngredientStack(theIngredient);
            for (Recipe recipe : Recipe.REGISTRY.values()) {
                boolean doThis = false;
                for (IngredientStack iStack : recipe.getRequired()) {
                    if (iStack.isSame(istacks)) {
                        doThis = true;
                        break;
                    }
                }

                if (doThis) {
                    list.add(recipe.getDisplayName());
                }
            }

            Collections.sort(list);
            for (String s: list) {
                builder.append("{{name|" + s + "}}\n");
            }

            return builder.toString();
        }

        return "N/A";
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        Crop crop = HFCrops.SEEDS.getCropFromStack(stack);
        int[] values = getStagesFromCrops(crop);
        if (values == null) return;
        StringBuilder builder = new StringBuilder();
        builder.append("===[[File:" + crop.getLocalizedName(true) + ".png]] [[" + crop.getLocalizedName(true) + "]]===\n" + DESCRIPTIONS.get(crop) + "\n" +
                "{| class=wikitable style=\"text-align:center;\" id=\"roundedborder\"\n" +
                "!Seeds\n");
        for (int i = 1; i < values.length; i++) {
            builder.append("!Stage " + i + "\n");
        }

        builder.append("!Harvest\n");
        if (crop.getRegrowStage() != 0) builder.append("!Regrow\n");
        builder.append(
                "!Sell Price\n" +
                "!Hunger\n" +
                "!Recipes\n" +
                "|-\n" +
                "| rowspan=2 style=\"text-align:right;\" \"|\n" +
                "{| \n" +
                "|\n" +
                "|<br>\n" +
                "[[File:" + crop.getSeedsName() + ".png|32px|center|link=" + crop.getSeedsName() + "]]\n" +
                "<center>[[" + crop.getSeedsName() + "]]</center><br>\n" +
                "[[General Store]] {{gold|" + crop.getSeedCost() + "}}<br>\n" +
                "|}\n");
        for (int i = 1; i <= values.length; i++) {
            builder.append("|[[File:" + crop.getLocalizedName(true) + " Stage " + i + ".png|center|64px|link=]]\n");
        }

        if (crop.getRegrowStage() != 0) builder.append("|[[File:" + crop.getLocalizedName(true) + " Stage " + (values.length - 1) + ".png|center|link=]]\n");
        builder.append(
                "|\n" +
                "{| \n" +
                "|[[File:" + crop.getLocalizedName(true) + ".png|32px|link=]]\n" +
                "|{{gold|" + crop.getSellValue() + "}}\n" +
                "|}\n" +
                "| rowspan=2 style=\"text-align:right;\" \"| ");

        String hunger = "N/A";
        ItemStack theCrop = crop.getCropStack(1);
        if (theCrop.getItem() instanceof ItemFood) {
            hunger = "{{hunger|" + ((ItemFood)theCrop.getItem()).getHealAmount(theCrop) + "}}";
        }

        builder.append(hunger);
        builder.append("\n" +
                "| rowspan=2 style=\"text-align:left;\" \"| ");

        builder.append(getRecipesForCrop(crop));
        builder.append(
                "|-\n");
        for (int i = 0; i < (values.length - 1); i++) {
            int previous = i - 1 <= 0 ? values[0] : values[i - 1];
            int current =  (i - 1 < 0) ? values[0] : values[i] - previous;
            builder.append("|" + current + " Days\n");
        }

        builder.append("|Total: " + crop.getStages() + " Days\n");
        if (crop.getRegrowStage() != 0) builder.append( "|Regrows every " + (crop.getStages() - crop.getRegrowStage()) + " days\n");
        int harvests = crop.getRegrowStage() == 0 ? 1: (int) Math.floor(1 + ((30D - crop.getStages())/(crop.getStages() - crop.getRegrowStage())));
        long gpd = ((harvests * crop.getSellValue() * 9) - crop.getSeedCost()) / (crop.getRegrowStage() == 0 ? crop.getStages() : 30);

        builder.append(
                "| {{gold|~" + gpd + "}}/d\n");
        builder.append(
                "|-\n" +
                "|}\n" +
                "\n");
        Debug.save(builder);
    }

    @Override
    public String getCommandName() {
        return "export-crops";
    }
}

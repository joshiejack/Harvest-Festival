package joshie.harvest.debug;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.item.ItemStack;

import java.util.List;

@HFCommand
@SuppressWarnings("unused")
public class CommandExportBuilder extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export";
    }

    @Override
    protected boolean isExportable(ItemStack stack) {
        return true;
    }


    private String getStore(ItemStack stack) {
        for (Shop shop: Shop.REGISTRY.values()) {
            for (IPurchasable purchasable: shop.getContents()) {
                if (purchasable.getDisplayStack().isItemEqual(stack)) {
                    return shop.getLocalizedName();
                }
            }
        }

        return null;
    }

    private long getCost(ItemStack stack) {
        for (Shop shop: Shop.REGISTRY.values()) {
            for (IPurchasable purchasable: shop.getContents()) {
                if (purchasable.getDisplayStack().isItemEqual(stack)) {
                    return purchasable.getCost();
                }
            }
        }

        return 0;
    }

    private StringBuilder getPurchasable(ItemStack stack) {
        StringBuilder builder = new StringBuilder();
        builder.append("{{Infobox purchasable\n" +
                "|image       = " + stack.getDisplayName() + ".png\n");
        if (getStore(stack) != null) {
            builder.append("|store        = [[" + getStore(stack) + "]]\n" +
                    "|cost = {{gold|" + getCost(stack) + "}}\n");
        }

        if (HFApi.shipping.getSellValue(stack) > 0L) {
            builder.append("|price = {{gold|" + HFApi.shipping.getSellValue(stack) + "}}\n");
        }

        if (CommandExportRecipe.getHunger(stack) != 0) {
            builder.append("|hunger = {{hunger|" + CommandExportRecipe.getHunger(stack) + "}}\n");
        }
                builder.append("}}");
        return builder;
    }

    private String getNavboxName(String... parameters) {
        for (String string: parameters) {
            if (string.startsWith("navbox")) return string.replace("navbox", "Navbox");
        }

        return null;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        List<String> strings = Lists.newArrayList(parameters);
        StringBuilder builder = new StringBuilder();
        if (strings.contains("ingredient") || strings.contains("all")) {
            builder.append(getPurchasable(stack));
            builder.append("\n");
        }

        if (strings.contains("gifts") || strings.contains("all")) {
            builder.append(CommandGiftExport.getGifts(stack));
            builder.append("\n");
        }

        if (strings.contains("recipes") || strings.contains("all")) {
            builder.append(CommandExportUsageInRecipes.getExport(stack));
            builder.append("\n");
        }

        String name = getNavboxName(parameters);
        if (name != null) {
            builder.append("{{" + name + "}}");
        }

        Debug.save(builder);
    }
}

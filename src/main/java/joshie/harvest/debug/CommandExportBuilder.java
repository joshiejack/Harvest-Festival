package joshie.harvest.debug;

import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.HFDebugCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportBuilder extends CommandExportHeld {
    public static final List<AbstractExport> commands = new ArrayList<>();

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (parameters.length == 0) {
            if (sender instanceof EntityPlayer) {
                ItemStack stack = ((EntityPlayer) sender).getHeldItemMainhand();
                if (stack != null) {
                    for (AbstractExport command : commands) {
                        if (command.isExportable(stack)) {
                            command.export(stack, parameters);
                            return true; //No more execution
                        }
                    }
                }
            }
        } return super.execute(server, sender, parameters);
    }

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
        builder.append("{{Infobox purchasable\n" + "|image       = ").append(stack.getDisplayName()).append(".png\n");
        if (getStore(stack) != null) {
            builder.append("|store        = [[").append(getStore(stack)).append("]]\n").append("|cost = {{gold|").append(getCost(stack)).append("}}\n");
        }

        if (HFApi.shipping.getSellValue(stack) > 0L) {
            builder.append("|price = {{gold|").append(HFApi.shipping.getSellValue(stack)).append("}}\n");
        }

        if (CommandExportRecipe.getHunger(stack) != 0) {
            builder.append("|hunger = {{hunger|").append(CommandExportRecipe.getHunger(stack)).append("}}\n");
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
            builder.append("{{").append(name).append("}}");
        }

        Debug.save(builder);
    }
}

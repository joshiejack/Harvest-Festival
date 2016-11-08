package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IPurchasableBuilder;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchasableBuilder extends PurchasableFML<BuildingImpl> implements IPurchasableBuilder {
    private final String resource;
    private ItemStack stack;
    private final int logs;
    private final int stone;

    public PurchasableBuilder(long cost, int logs, int stone, ResourceLocation name) {
        super(cost, name);
        this.logs = logs;
        this.stone = stone;
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + name.toString().replace(":", "_");
    }

    public PurchasableBuilder(long cost, int logs, int stone, ItemStack stack) {
        super(cost, null);
        this.logs = logs;
        this.stone = stone;
        this.stack = stack;
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + Purchasable.stackToString(stack);
    }

    @Override
    public IForgeRegistry<BuildingImpl> getRegistry() {
        return BuildingRegistry.REGISTRY;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "logWood", (getLogCost() * amount))
                && InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "stone", (getStoneCost() * amount)) && isPurchaseable(world, player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack != null ? stack : HFBuildings.BLUEPRINTS.getStackFromObject(item);
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "logWood", getLogCost());
        InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "stone", getStoneCost());
        super.onPurchased(player);
    }

    public boolean isPurchaseable(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public int getLogCost() {
        return logs;
    }

    @Override
    public int getStoneCost() {
        return stone;
    }

    @Override
    public String getDisplayName() {
        return stack != null ? stack.getDisplayName() : getDisplayStack().getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + getDisplayName());
        if (this.tooltip != null) {
            list.add("---------------------------");
            String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
            list.addAll(Arrays.asList(tooltip.split("\r\n")));
        }
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}

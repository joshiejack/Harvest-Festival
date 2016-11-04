package joshie.harvest.shops.purchasable;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.core.helpers.InventoryHelper.ORE_DICTIONARY;

public class PurchasableBuilder extends PurchasableFML<BuildingImpl> {
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
    public boolean canBuy(World world, EntityPlayer player) {
        if (!InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "logWood", getLogCost())) return false;
        if (!InventoryHelper.hasInInventory(player, ORE_DICTIONARY, "stone", getStoneCost())) return false;
        return isPurchaseable(world, player);
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
    public boolean onPurchased(EntityPlayer player) {
        if (getCost() < 0) {
            return !InventoryHelper.hasInInventory(player, ITEM_STACK, getDisplayStack(), getDisplayStack().stackSize)
                    || !InventoryHelper.takeItemsInInventory(player, ITEM_STACK, getDisplayStack(), getDisplayStack().stackSize);
        } else {
            InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "logWood", getLogCost());
            InventoryHelper.takeItemsInInventory(player, ORE_DICTIONARY, "stone", getStoneCost());
            return super.onPurchased(player);
        }
    }

    public boolean isPurchaseable(World world, EntityPlayer player) {
        return true;
    }

    public int getLogCost() {
        return logs;
    }

    public int getStoneCost() {
        return stone;
    }

    public String getName() {
        return stack != null ? stack.getDisplayName() : getDisplayStack().getDisplayName();
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}

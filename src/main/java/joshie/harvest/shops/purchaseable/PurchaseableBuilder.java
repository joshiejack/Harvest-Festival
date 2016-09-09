package joshie.harvest.shops.purchaseable;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class PurchaseableBuilder extends PurchaseableFML<BuildingImpl> {
    private String resource;
    private ItemStack stack;
    private final int logs;
    private final int stone;

    public PurchaseableBuilder(long cost, int logs, int stone, ResourceLocation name) {
        super(cost, name);
        this.logs = logs;
        this.stone = stone;
        this.resource = name.toString().replace(":", "_");
    }

    public PurchaseableBuilder(long cost, int logs, int stone, ItemStack stack) {
        super(cost, null);
        this.logs = logs;
        this.stone = stone;
        this.stack = stack;
        this.resource = Purchaseable.stackToString(stack);
    }

    @Override
    public IForgeRegistry<BuildingImpl> getRegistry() {
        return BuildingRegistry.REGISTRY;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        int wood = InventoryHelper.getCount(player, "logWood");
        if (wood < getLogCost()) return false;
        int stone = InventoryHelper.getCount(player, "stone");
        if (stone < getStoneCost()) return false;
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
        int logs = 0;
        int stone = 0;

        for (int i = 0; i < player.inventory.mainInventory.length && (logs < getLogCost() || stone < getStoneCost()); i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null) {
                if (InventoryHelper.isOreName(stack, "logWood")) {
                    for (int j = 0; j < 64 && (logs < getLogCost()) && stack.stackSize > 0; j++) {
                        player.inventory.decrStackSize(i, 1);
                        logs++;
                    }
                } else if (InventoryHelper.isOreName(stack, "stone")) {
                    for (int j = 0; j < 64 && (stone < getStoneCost()) && stack.stackSize > 0; j++) {
                        player.inventory.decrStackSize(i, 1);
                        stone++;
                    }
                }
            }
        }

        return super.onPurchased(player);
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

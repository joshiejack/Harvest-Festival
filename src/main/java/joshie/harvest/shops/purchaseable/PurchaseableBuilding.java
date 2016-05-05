package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PurchaseableBuilding extends Purchaseable {
    public static List<PurchaseableBuilding> listings = new ArrayList<PurchaseableBuilding>();
    private IBuilding building;

    public PurchaseableBuilding(IBuilding building) {
        super(building.getCost(), building.getProvider().getPreview());
        this.building = building;
        listings.add(this);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        int wood = InventoryHelper.getCount(player, "logWood");
        if (wood < building.getWoodCount()) return false;
        int stone = InventoryHelper.getCount(player, "stone");
        if (stone < building.getStoneCount()) return false;
        return building.canBuy(world, player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return !HFTrackers.getPlayerTracker(player).getTown().hasBuilding(building) && building.canBuy(world, player);
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

    public int getLogCost() {
        return building.getWoodCount();
    }

    public int getStoneCost() {
        return building.getStoneCount();
    }

    public String getName() {
        return stacks[0].getDisplayName();
    }
}

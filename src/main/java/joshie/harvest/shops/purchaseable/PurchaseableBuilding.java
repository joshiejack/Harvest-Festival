package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.HFApi;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.town.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PurchaseableBuilding extends Purchaseable {
    public static List<PurchaseableBuilding> listings = new ArrayList<>();
    private ResourceLocation resource;
    private Building building;

    public PurchaseableBuilding(Building building) {
        super(building.getCost(), building.getBlueprint());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getNameForObject(building);
        listings.add(this);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToPlayer(player);
        if (town.hasBuilding(HFApi.buildings.getNameForBuilding(building))) return false;
        int wood = InventoryHelper.getCount(player, "logWood");
        if (wood < building.getWoodCount()) return false;
        int stone = InventoryHelper.getCount(player, "stone");
        if (stone < building.getStoneCount()) return false;
        return building.getRules().canBuy(world, player) && building.hasRequirements(player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return !HFTrackers.getTownTracker(world).getClosestTownToBlockPos(player.dimension, new BlockPos(player)).hasBuilding(resource) && building.getRules().canBuy(world, player) && building.hasRequirements(player);
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

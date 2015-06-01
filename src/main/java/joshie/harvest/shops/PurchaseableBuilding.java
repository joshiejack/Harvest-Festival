package joshie.harvest.shops;

import java.util.ArrayList;
import java.util.List;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PurchaseableBuilding extends Purchaseable {
    public static List<PurchaseableBuilding> listings = new ArrayList();
    private IBuilding building;

    public PurchaseableBuilding(Building building) {
        super(building.getCost(), building.getPreview());
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
        return !TownHelper.hasBuilding(UUIDHelper.getPlayerUUID(player), building) && building.canBuy(world, player);
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

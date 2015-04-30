package joshie.harvestmoon.shops;

import joshie.harvestmoon.buildings.BuildingGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PurchaseableBuilding extends Purchaseable {
    private BuildingGroup building;
    
    public PurchaseableBuilding(int cost, BuildingGroup building) {
        super(cost, building.getPreview());
        this.building = building;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return building.canBuy(world, player);
    }
}

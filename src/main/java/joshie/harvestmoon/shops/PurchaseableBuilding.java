package joshie.harvestmoon.shops;

import joshie.harvestmoon.buildings.Building;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PurchaseableBuilding extends Purchaseable {
    private Building building;
    
    public PurchaseableBuilding(int cost, Building building) {
        super(cost, building.getPreview());
        this.building = building;
    }
    
    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return building.canBuy(world, player);
    }
}

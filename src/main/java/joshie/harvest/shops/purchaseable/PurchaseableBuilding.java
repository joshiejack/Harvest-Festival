package joshie.harvest.shops.purchaseable;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PurchaseableBuilding extends PurchaseableBuilder {
    private final ResourceLocation resource;
    private final BuildingImpl building;

    public PurchaseableBuilding(BuildingImpl building) {
        super(building.getCost(), building.getWoodCount(), building.getStoneCount(), building.getRegistryName());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getKey(building);
    }

    @Override
    public boolean isPurchaseable(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player);
        if (town.hasBuilding(resource)) return false;
        return building.getRules().canBuy(world, player) && building.hasRequirements(player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return !HFTrackers.getTownTracker(world).getClosestTownToBlockPos(new BlockPos(player)).hasBuilding(resource) && building.getRules().canBuy(world, player) && building.hasRequirements(player);
    }

    @Override
    public String getName() {
        return building.getLocalisedName();
    }
}

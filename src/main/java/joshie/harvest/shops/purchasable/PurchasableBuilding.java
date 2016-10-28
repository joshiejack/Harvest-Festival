package joshie.harvest.shops.purchasable;

import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PurchasableBuilding extends PurchasableBuilder {
    private final ResourceLocation resource;
    private final BuildingImpl building;

    public PurchasableBuilding(BuildingImpl building) {
        super(building.getCost(), building.getWoodCount(), building.getStoneCount(), building.getRegistryName());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getKey(building);
    }

    @Override
    public ItemStack getDisplayStack() {
        return HFBuildings.CHEAT_BUILDINGS ? building.getSpawner() : building.getBlueprint();
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

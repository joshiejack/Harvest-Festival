package joshie.harvest.shops.purchasable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PurchasableBuilding extends PurchasableBuilder {
    private final Cache<EntityPlayer, TownData> closestCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(128).build();
    private final ResourceLocation resource;
    private final BuildingImpl building;

    public PurchasableBuilding(BuildingImpl building) {
        super(building.getCost(), building.getWoodCount(), building.getStoneCount(), building.getRegistryName());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getKey(building);
    }

    private TownData getClosestTownToPlayer(EntityPlayer player) {
        try {
            return closestCache.get(player, () -> TownHelper.getClosestTownToEntity(player));
        } catch (ExecutionException ex) { return null; }
    }

    @Override
    public ItemStack getDisplayStack() {
        return HFBuildings.CHEAT_BUILDINGS ? building.getSpawner() : building.getBlueprint();
    }

    @Override
    public boolean isPurchaseable(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player);
        if (town.hasBuilding(resource)) return false;
        return building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        TownData town = getClosestTownToPlayer(player);
        return town != null && !town.hasBuilding(resource) && building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public String getName() {
        return building.getLocalisedName();
    }
}

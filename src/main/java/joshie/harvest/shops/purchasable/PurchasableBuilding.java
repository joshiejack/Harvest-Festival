package joshie.harvest.shops.purchasable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PurchasableBuilding extends PurchasableBuilder {
    private final Cache<EntityPlayer, TownData> closestCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(128).build();
    private final ResourceLocation resource;
    private final BuildingImpl building;
    private final String tooltip;

    public PurchasableBuilding(BuildingImpl building) {
        super(building.getCost(), building.getWoodCount(), building.getStoneCount(), building.getRegistryName());
        this.building = building;
        this.resource = BuildingRegistry.REGISTRY.getKey(building);
        this.tooltip = resource.getResourceDomain() + ".structures." + resource.getResourcePath() + ".tooltip";
    }

    private TownData getClosestTownToPlayer(EntityPlayer player) {
        try {
            return closestCache.get(player, () -> TownHelper.getClosestTownToEntity(player));
        } catch (ExecutionException ex) { return null; }
    }

    @Override
    public ItemStack getDisplayStack() {
        return building.getSpawner();
    }

    @Override
    public boolean isPurchaseable(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player);
        if (town.hasBuilding(resource)) return false;
        return building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return amount == 1 && super.canBuy(world, player, amount);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        TownData town = getClosestTownToPlayer(player);
        return town != null && !town.hasBuilding(resource) && building.getRules().canBuy(world, player, 1) && building.hasRequirements(player);
    }

    @Override
    public String getDisplayName() {
        return building.getLocalisedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(TextFormatting.GOLD + getDisplayName());
        list.add("---------------------------");
        String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
        list.addAll(Arrays.asList(tooltip.split("\r\n")));
    }
}

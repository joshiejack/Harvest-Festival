package joshie.harvest.shops.purchasable;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.buildings.HFBuildings;
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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PurchasableBuilding extends PurchasableMaterials {
    private final Building building;
    private final String tooltip;

    public PurchasableBuilding(long cost, Building building, IRequirement... requirements) {
        super(requirements);
        this.building = building;
        this.cost = cost;
        this.tooltip = building.getResource().getNamespace() + ".structures." + building.getResource().getPath() + ".tooltip";
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + building.getResource().toString().replace(":", "_");
    }

    @Override
    @Nonnull
    public ItemStack getDisplayStack() {
        return building.getSpawner();
    }

    @Override
    @Nonnull
    protected ItemStack getPurchasedStack() {
        return HFBuildings.CHEAT_BUILDINGS ? building.getSpawner() : building.getBlueprint();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isPurchasable(World world, EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        return !town.hasBuilding(building) && building.getRules().canDo(world, player, 1) && hasBuildingRequirements(player);
    }

    private boolean hasBuildingRequirements(EntityPlayer player) {
        ResourceLocation[] requirements = building.getRequirements();
        return requirements.length == 0 || TownHelper.getClosestTownToEntity(player, false).hasBuildings(requirements);
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return amount == 1 && super.canDo(world, player, amount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return (!TownHelper.getClosestTownToEntity(player, false).hasBuilding(building) || building.canHaveMultiple())
                && building.getRules().canDo(world, player, 1) && hasBuildingRequirements(player);
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

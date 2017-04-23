package joshie.harvest.shops.purchasable;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.building.BuildingFestival;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownBuilding;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class PurchasableDestroy extends Purchasable {
    private final Building building;

    public PurchasableDestroy(long cost, Building building) {
        super(cost, null);
        this.building = building;
        this.cost = cost;
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + building.getResource().toString().replace(":", "_");
    }

    @Override
    public ItemStack getDisplayStack() {
        return building.getSpawner();
    }

    @Override
    protected ItemStack getPurchasedStack() {
        return building.getSpawner();
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return amount == 1 && TownHelper.getClosestTownToEntity(player, false).hasBuilding(building);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return TownHelper.getClosestTownToEntity(player, false).hasBuilding(building);
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        TownData town = TownHelper.getClosestTownToEntity(player, false);
        TownBuilding theBuilding = town.getBuilding(building);
        HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
        if (template != null && theBuilding != null) {
            template.removeBlocks(player.worldObj, theBuilding.pos, theBuilding.rotation, Blocks.AIR.getDefaultState(), true);
            if (theBuilding.building == HFBuildings.FESTIVAL_GROUNDS) {
                BuildingFestival.getFestivalTemplateFromFestival(town.getFestival()).removeBlocks(player.worldObj, theBuilding.pos, theBuilding.rotation, Blocks.AIR.getDefaultState(), true);
            }

            TownHelper.<TownDataServer>getClosestTownToEntity(player, false).removeBuilding(theBuilding);
        }
    }

    @Override
    public String getDisplayName() {
        return TextHelper.formatHF("structures.demolish", building.getLocalisedName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(TextFormatting.RED + TextHelper.translate("structures.warning1"));
        list.add(TextFormatting.RED + TextHelper.translate("structures.warning2"));
        list.add(TextFormatting.RED + TextHelper.translate("structures.warning3"));
    }
}

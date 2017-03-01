package joshie.harvest.buildings.item;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.BuildingError;
import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.render.BuildingKey;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFRegistry;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilding extends ItemHFRegistry<ItemBuilding, Building> implements ICreativeSorted {
    public ItemBuilding() {
        super("Building", BuildingRegistry.REGISTRY, Building.REGISTRY, HFTab.TOWN);
        setMaxStackSize(1);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        List<BuildingError> errors = new ArrayList<>();
        if (world.provider.getDimension() != 0) errors.add(BuildingError.DIMENSION);
        else {
            Building building = getObjectFromStack(stack);
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128D, 0F);
            if (raytrace == null || building == null) return new ActionResult<>(EnumActionResult.PASS, stack); //Skip the rest of this
            BlockPos pos = raytrace.getBlockPos();
            if (player.canPlayerEdit(pos, EnumFacing.DOWN, stack)) {
                TownData town = TownHelper.getClosestTownToBlockPos(world, pos, false);
                if ((!town.hasBuilding(building) && !town.isBuilding(building)) || building.canHaveMultiple() || HFCore.DEBUG_MODE) {
                    BuildingKey key = BuildingHelper.getPositioning(stack, world, raytrace, building, player, true);
                    if (key != null) {
                        if (!world.isRemote) {
                            HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
                            if (template != null) {
                                template.placeBlocks(world, key.getPos(), key.getRotation(), building);
                                if (HFBuildings.CHEAT_BUILDINGS) {
                                    TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos, false).createOrUpdateBuilder((WorldServer) world, pos);
                                }
                            }
                        }

                        stack.splitStack(1); //Decrease the stack size
                    } else return new ActionResult<>(EnumActionResult.PASS, stack);
                } else if (town.hasBuilding(building)) errors.add(BuildingError.DISTANCE);
                else errors.add(BuildingError.BUILDING);
            } else errors.add(BuildingError.PERMISSION);
        }

        //Process the errors for the player
        if (errors.size() == 0) return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        else return BuildingHelper.displayErrors(world, stack, errors);
    }

    @Override
    public Building getDefaultValue() {
        return HFBuildings.CARPENTER;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextHelper.format("harvestfestival.structures.spawn", getObjectFromStack(stack).getLocalisedName());
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 200;
    }
}

package joshie.harvest.buildings.item;

import joshie.harvest.buildings.*;
import joshie.harvest.buildings.render.BuildingKey;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilding extends ItemHFFML<ItemBuilding, BuildingImpl> implements ICreativeSorted {
    public ItemBuilding() {
        super(BuildingRegistry.REGISTRY, HFTab.TOWN);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        List<BuildingError> errors = new ArrayList<>();
        if (world.provider.getDimension() != 0) errors.add(BuildingError.DIMENSION);
        else {
            BuildingImpl building = getObjectFromStack(stack);
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128D, 0F);
            if (raytrace == null || building == null) return new ActionResult<>(EnumActionResult.PASS, stack); //Skip the rest of this
            BlockPos pos = raytrace.getBlockPos();
            if (player.canPlayerEdit(pos, EnumFacing.DOWN, stack)) {
                TownData town = TownHelper.getClosestTownToBlockPos(world, pos);
                TownHelper.ensureBuilderExists(world, pos, town);
                if ((!town.hasBuilding(building) && !town.isBuilding(building)) || building.canHaveMultiple()) {
                    BuildingKey key = BuildingHelper.getPositioning(stack, world, raytrace, building, player, true);
                    if (key != null) {
                        if (!world.isRemote) {
                            building.generate(world, key.getPos(), key.getRotation());
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
        /*


        if (world.provider.getDimension() == 0) {
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 0F);
            if (raytrace == null || raytrace.getBlockPos() == null) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            BuildingImpl building = getObjectFromStack(stack);
            if (building != null && (DEBUG_MODE || building.canHaveMultiple() || !TownHelper.getClosestTownToEntity(player).hasBuilding(building.getRegistryName()))) {
                if(player.canPlayerEdit(raytrace.getBlockPos(), EnumFacing.DOWN, stack)) {
                    if (!world.isRemote) {
                        TownHelper.ensureBuilderExists(world, raytrace.getBlockPos(), null); //Force a town to exist near where you clicked
                    }

                    BuildingKey key = BuildingHelper.getPositioning(stack, world, raytrace, building, player, true);
                    if (key != null) {
                        stack.splitStack(1);
                        return new ActionResult<>(building.generate(world, key.getPos(), key.getRotation()), stack);
                    }
                } else if (world.isRemote) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("town.failure") + " " + TextFormatting.WHITE + TextHelper.translate("town.permission"));
            } else if (world.isRemote) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("town.failure") + " " + TextFormatting.WHITE + TextHelper.translate("town.distance"));
        } else if (world.isRemote) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("town.failure") + " " + TextFormatting.WHITE + TextHelper.translate("town.dimension"));
        return new ActionResult<>(EnumActionResult.PASS, stack); */
    }

    @Override
    public BuildingImpl getNullValue() {
        return HFBuildings.null_building;
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

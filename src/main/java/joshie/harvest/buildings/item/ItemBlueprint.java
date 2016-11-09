package joshie.harvest.buildings.item;

import joshie.harvest.buildings.*;
import joshie.harvest.buildings.render.BuildingKey;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemBlueprint extends ItemHFFML<ItemBlueprint, BuildingImpl> implements ICreativeSorted {
    public ItemBlueprint() {
        super(BuildingRegistry.REGISTRY, HFTab.TOWN);
        setMaxStackSize(1);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
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
                            ((TownDataServer)town).setBuilding(world, building, key.getPos().down(building.getOffsetY()), key.getRotation());
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


        /*if (world.provider.getDimension() == 0) {
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 0F);
            if (raytrace == null || raytrace.getBlockPos() == null) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            BuildingImpl building = getObjectFromStack(stack);
            TownData town = TownHelper.getClosestTownToEntity(player);
            if(player.canPlayerEdit(raytrace.getBlockPos(), EnumFacing.DOWN, stack)) {
                if (!world.isRemote) TownHelper.ensureBuilderExists(world, raytrace.getBlockPos()); //Force a town to exist near where you clicked
                if (building != null && (DEBUG_MODE || building.canHaveMultiple() || (!town.hasBuilding(building.getRegistryName()) && !town.isBuilding(building)))) {
                    BuildingKey key = BuildingHelper.getPositioning(stack, world, raytrace, building, player, true);
                    if (key != null) {
                        if (!TownHelper.getClosestTownToBlockPos(world, key.getPos()).isBuilding(building)) {
                            if (!world.isRemote) {
                                Rotation rotation = key.getRotation();
                                EntityNPCBuilder builder = TownHelper.<TownDataServer>getClosestTownToEntity(player).getBuilder((WorldServer) world);
                                BlockPos pos = key.getPos();
                                if (builder != null && !TownHelper.getClosestTownToEntity(player).hasBuilding(building.getRegistryName())) {
                                    if (TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).setBuilding(world, building, pos.down(building.getOffsetY()), rotation)) {
                                        if (builder.getBuilding() == null) builder.setPosition(pos.getX(), pos.up().getY(), pos.getZ()); //Teleport the builder to the position
                                    }
                                }
                            }

                            stack.splitStack(1);
                            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                        } else if (world.isRemote) ChatHelper.displayChat(TextFormatting.RED + TextHelper.translate("town.failure") + " " + TextFormatting.WHITE + TextHelper.translate("town.building"));
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
        return TextHelper.format("harvestfestival.structures.blueprint", getObjectFromStack(stack).getLocalisedName());
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 105;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (BuildingImpl building: registry) {
            ModelLoader.setCustomModelResourceLocation(item, registry.getValues().indexOf(building), new ModelResourceLocation(getRegistryName(), "inventory"));
        }
    }
}

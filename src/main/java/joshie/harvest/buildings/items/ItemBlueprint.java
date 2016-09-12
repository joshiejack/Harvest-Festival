package joshie.harvest.buildings.items;

import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.render.BuildingKey;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.util.Direction;
import joshie.harvest.core.util.ICreativeSorted;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownHelper;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlueprint extends ItemHFFML<ItemBlueprint, BuildingImpl> implements ICreativeSorted {
    public ItemBlueprint() {
        super(BuildingRegistry.REGISTRY, HFTab.TOWN);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (world.provider.getDimension() == 0) {
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 128, 1F);
            if (raytrace == null || raytrace.getBlockPos() == null || raytrace.sideHit != EnumFacing.UP) {
                return new ActionResult(EnumActionResult.PASS, stack);
            }

            if (!world.isRemote) TownHelper.ensureTownExists(world, raytrace.getBlockPos());
            BuildingImpl building = getObjectFromStack(stack);
            if (player.canPlayerEdit(raytrace.getBlockPos(), EnumFacing.DOWN, stack) && building != null && (building.canHaveMultiple() || !TownHelper.getClosestTownToEntity(player).hasBuilding(building.getRegistryName()))) {
                BuildingKey key = BuildingHelper.getPositioning(world, raytrace, building, player, hand);
                if (!TownHelper.getClosestTownToBlockPos(world, key.getPos()).isBuilding(building)) {
                    if (!world.isRemote) {
                        Direction direction = Direction.withMirrorAndRotation(key.getMirror(), key.getRotation());
                        EntityNPCBuilder builder = TownHelper.<TownDataServer>getClosestTownToEntity(player).getBuilder((WorldServer) world);
                        BlockPos pos = key.getPos();
                        if (builder != null && !TownHelper.getClosestTownToEntity(player).hasBuilding(building.getRegistryName())) {
                            if (TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).setBuilding(world, building, pos.down(building.getOffsetY()), direction.getMirror(), direction.getRotation())) {
                                if (builder.getBuilding() == null)
                                    builder.setPosition(pos.getX(), pos.up().getY(), pos.getZ()); //Teleport the builder to the position
                            }
                        }
                    }

                    stack.splitStack(1);
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                } else ChatHelper.displayChat(TextFormatting.RED + Text.translate("town.failure") + " " + TextFormatting.WHITE + Text.translate("town.distance"));
            } else ChatHelper.displayChat(TextFormatting.RED + Text.translate("town.failure") + " " + TextFormatting.WHITE + Text.translate("town.distance"));
        } else if (world.isRemote) {
            ChatHelper.displayChat(TextFormatting.RED + Text.translate("town.failure") + " " + TextFormatting.WHITE + Text.translate("town.dimension"));
        }

        return new ActionResult(EnumActionResult.PASS, stack);
    }

    @Override
    public BuildingImpl getNullValue() {
        return HFBuildings.null_building;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.format("harvestfestival.structures.blueprint", getObjectFromStack(stack).getLocalisedName());
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

package joshie.harvest.buildings.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.util.Direction;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.util.base.ItemHFFML;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemBlueprint extends ItemHFFML<ItemBlueprint, Building> implements ICreativeSorted {
    public ItemBlueprint() {
        super(BuildingRegistry.REGISTRY, HFTab.TOWN);
        setMaxDamage(32000);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //Create a builder
        if (!world.isRemote) {
            TownHelper.getClosestBuilderToEntityOrCreate(player);
        }

        Building building = getObjectFromStack(stack);
        if (building != null) {
            Mirror mirror = hand == EnumHand.OFF_HAND ? player.isSneaking() ? Mirror.FRONT_BACK : Mirror.LEFT_RIGHT : Mirror.NONE;
            Rotation rotation = Rotation.NONE;
            if (facing == EnumFacing.NORTH) {
                rotation = Rotation.NONE;
            } else if (facing == EnumFacing.SOUTH) {
                rotation = Rotation.CLOCKWISE_180;
            } else if (facing == EnumFacing.EAST) {
                rotation = Rotation.COUNTERCLOCKWISE_90;
            } else if (facing == EnumFacing.WEST) {
                rotation = Rotation.CLOCKWISE_90;
            }

            Direction direction = Direction.withMirrorAndRotation(mirror, rotation);
            EntityNPCBuilder builder = TownHelper.getClosestBuilderToEntityOrCreate(player);
            if (builder != null && !builder.isBuilding()) {
                builder.setPosition(pos.getX(), pos.up().getY(), pos.getZ()); //Teleport the builder to the position
                builder.startBuilding(building, pos.up(), direction.getMirror(), direction.getRotation());
                stack.splitStack(1);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public Building getNullValue() {
        return HFBuildings.null_building;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getObjectFromStack(stack).getLocalisedName();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 105;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, "blueprint"), "inventory"));
    }
}

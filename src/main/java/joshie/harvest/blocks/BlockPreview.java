package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.loader.HFBuildings;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;

public class BlockPreview extends BlockHFBaseEnum<Direction> {
    public ItemStack getItemStack(IBuilding building) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Building", building.getResource().toString());
        return stack;
    }

    public enum Direction implements IStringSerializable {
        MN_R0(Mirror.NONE, Rotation.NONE),
        MN_R90(Mirror.NONE, Rotation.CLOCKWISE_90),
        MN_R180(Mirror.NONE, Rotation.CLOCKWISE_180),
        MN_R270(Mirror.NONE, Rotation.COUNTERCLOCKWISE_90),
        MLR_R0(Mirror.LEFT_RIGHT, Rotation.NONE),
        MLR_R90(Mirror.LEFT_RIGHT, Rotation.CLOCKWISE_90),
        MLR_R180(Mirror.LEFT_RIGHT, Rotation.CLOCKWISE_180),
        MLR_R270(Mirror.LEFT_RIGHT, Rotation.COUNTERCLOCKWISE_90),
        MFB_R0(Mirror.FRONT_BACK, Rotation.NONE),
        MFB_R90(Mirror.FRONT_BACK, Rotation.CLOCKWISE_90),
        MFB_R180(Mirror.FRONT_BACK, Rotation.CLOCKWISE_180),
        MFB_R270(Mirror.FRONT_BACK, Rotation.COUNTERCLOCKWISE_90);

        public static final HashMap<Pair<Mirror, Rotation>, Direction> map = new HashMap<Pair<Mirror, Rotation>, Direction>();
        private final Mirror mirror;
        private final Rotation rotation;

        Direction(Mirror mirror, Rotation rotation) {
            this.mirror = mirror;
            this.rotation = rotation;
        }

        public Mirror getMirror() {
            return this.mirror;
        }

        public Rotation getRotation() {
            return this.rotation;
        }

        public IBlockState withDirection(IBlockState state) {
            return state.withMirror(mirror).withRotation(rotation);
        }

        public static Direction withMirrorAndRotation(Mirror mirror, Rotation rotation) {
            return map.get(Pair.of(mirror, rotation));
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    static {
        for (Direction direction: Direction.values()) {
            Direction.map.put(Pair.of(direction.getMirror(), direction.getRotation()), direction);
        }
    }

    public BlockPreview() {
        super(Material.WOOD, Direction.class, HFTab.TOWN);
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            int next = getEnumFromState(state).ordinal() + 1;
            if (next >= 11) {
                next = 0;
            }

            IBuilding building =  ((TileMarker)world.getTileEntity(pos)).getBuilding();
            if (building != null) {
                world.setBlockState(pos, getStateFromMeta(next), 2);
                ((TileMarker)world.getTileEntity(pos)).setBuilding(building);
                return true;
            } else return false;
        } else {
            Direction direction = getEnumFromState(state);
            TileMarker marker = (TileMarker) world.getTileEntity(pos);
            EntityNPCBuilder builder = TownHelper.getClosestBuilderToEntityOrCreate(player);
            if (builder != null && !builder.isBuilding()) {
                builder.setPosition(pos.getX(), pos.getY(), pos.getZ()); //Teleport the builder to the position
                builder.startBuilding(marker.getBuilding(), pos, direction.getMirror(), direction.getRotation());
                world.setBlockToAir(pos);
                return true;
            } else return false;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMarker();
    }

    public IBuilding getBuildingFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return HFBuildings.null_building;
        IBuilding building = HFApi.buildings.getBuildingFromName(new ResourceLocation(stack.getTagCompound().getString("Building")));
        return building == null ? HFBuildings.null_building: building;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        if (!(player instanceof EntityPlayer)) return;
        IBuilding building = getBuildingFromStack(stack);
        if (building != null) {
            TileMarker marker = (TileMarker) world.getTileEntity(pos);
            marker.setBuilding(building);
            //Create a builder if none exists
            if (!world.isRemote) {
                TownHelper.getClosestBuilderToEntityOrCreate(player);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TileMarker marker = (TileMarker) world.getTileEntity(pos);
        IBuilding building = marker.getBuilding();
        if (building == null) return null;
        else {
            return getItemStack(building);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (tab != HFTab.TOWN) return;
        if (General.DEBUG_MODE) {
            for (IBuilding building: HFApi.buildings.getBuildings()) {
                list.add(getItemStack(building));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getBuildingFromStack(stack).getLocalisedName();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 105;
    }
}
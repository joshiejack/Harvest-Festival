package joshie.harvest.mining.block;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFBase;
import joshie.harvest.core.util.Text;
import joshie.harvest.mining.HFMining;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static joshie.harvest.mining.block.BlockDirt.TextureStyle.*;

public class BlockDirt extends BlockHFBase<BlockDirt> {
    public static final PropertyEnum<TextureStyle> NORTH_EAST = PropertyEnum.create("ne", TextureStyle.class);
    public static final PropertyEnum<TextureStyle> NORTH_WEST = PropertyEnum.create("nw", TextureStyle.class);
    public static final PropertyEnum<TextureStyle> SOUTH_EAST = PropertyEnum.create("se", TextureStyle.class);
    public static final PropertyEnum<TextureStyle> SOUTH_WEST = PropertyEnum.create("sw", TextureStyle.class);

    public enum TextureStyle implements IStringSerializable {
        BLANK, INNER, VERTICAL, HORIZONTAL, OUTER;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockDirt() {
        super(Material.GROUND, HFTab.MINING);
        setSoundType(SoundType.GROUND);
        setDefaultState(blockState.getBaseState()
                .withProperty(NORTH_EAST, OUTER)
                .withProperty(NORTH_WEST, OUTER)
                .withProperty(SOUTH_EAST, OUTER)
                .withProperty(SOUTH_WEST, OUTER));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localizeFully(getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItem() == Item.getItemFromBlock(HFMining.DIRT_DECORATIVE)) list.add(TextFormatting.YELLOW + Text.translate("tooltip.cosmetic"));
    }

    //TECHNICAL/
    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return blockHardness != -1F;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
        return plantable.getPlantType(world, pos.up()) == EnumPlantType.Plains;
    }

    private boolean isSameBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().getClass() == this.getClass();
    }

    private TextureStyle getStateFromBoolean(boolean one, boolean two, boolean three) {
        if (one && !two && !three) return VERTICAL;
        if (!one && two && !three) return  HORIZONTAL;
        if (one && two && !three) return INNER;
        if (!one && two) return HORIZONTAL;
        if (one && !two) return VERTICAL;
        if (one) return BLANK;
        return OUTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public static final Cache<BlockPos, IBlockState> CACHE_STATE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(1024).build();

    @SuppressWarnings("deprecation, unchecked")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            return CACHE_STATE.get(pos, new Callable<IBlockState>() {
                @Override
                public IBlockState call() throws Exception {
                    boolean north = isSameBlock(world, pos.north());
                    boolean west = isSameBlock(world, pos.west());
                    boolean south = isSameBlock(world, pos.south());
                    boolean east = isSameBlock(world, pos.east());
                    boolean northEast = north && east && isSameBlock(world, pos.north().east());
                    boolean northWest = north && west && isSameBlock(world, pos.north().west());
                    boolean southEast = south && east && isSameBlock(world, pos.south().east());
                    boolean southWest = south && west && isSameBlock(world, pos.south().west());
                    TextureStyle ne = getStateFromBoolean(north, east, northEast);
                    TextureStyle nw = getStateFromBoolean(north, west, northWest);
                    TextureStyle se = getStateFromBoolean(south, east, southEast);
                    TextureStyle sw = getStateFromBoolean(south, west, southWest);
                    return state.withProperty(NORTH_EAST, ne).withProperty(NORTH_WEST, nw).withProperty(SOUTH_EAST, se).withProperty(SOUTH_WEST, sw);
                }
            });
        } catch (Exception e) { return state; }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "ne=outer,nw=outer,se=outer,sw=outer"));
    }
}
package joshie.harvest.mining.blocks;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.WorldHelper;
import joshie.harvest.core.base.BlockHFBase;
import joshie.harvest.core.util.Text;
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
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.mining.blocks.BlockDirt.TextureStyle.*;

public class BlockDirt extends BlockHFBase<BlockDirt> {
    public static final PropertyEnum NORTH_EAST = PropertyEnum.create("ne", TextureStyle.class);
    public static final PropertyEnum NORTH_WEST = PropertyEnum.create("nw", TextureStyle.class);
    public static final PropertyEnum SOUTH_EAST = PropertyEnum.create("se", TextureStyle.class);
    public static final PropertyEnum SOUTH_WEST = PropertyEnum.create("sw", TextureStyle.class);
    public static final PropertyEnum TEXTURE = PropertyEnum.create("texture", TextureType.class);

    public enum TextureType implements IStringSerializable {
        BLANK(150), BONES1(1), BONES2(1), BONES3(1), BONES4(1), GRASS1(7), LEAVES1(4), LEAVES2(4), LEAVES3(1), LEAVES4(1),
        PEBBLE1(4), PEBBLE2(4), PEBBLE3(3), PEBBLE4(1), PEBBLE5(2), PEBBLE6(2), ROCK1(4), ROCK2(4);

        private final int weight;

        private TextureType(int weight) {
            this.weight = weight;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public enum TextureStyle implements IStringSerializable {
        BLANK, INNER, VERTICAL, HORIZONTAL, OUTER;
        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static class WeightedTexture extends WeightedRandom.Item {
        public TextureType type;

        public WeightedTexture(TextureType type) {
            super(type.weight);
            this.type = type;
        }
    }

    protected final List<WeightedTexture> textures;
    protected final int totalWeight;

    public BlockDirt() {
        super(Material.GROUND, HFTab.MINING);
        setSoundType(SoundType.GROUND);
        setDefaultState(blockState.getBaseState()
                .withProperty(NORTH_EAST, OUTER)
                .withProperty(NORTH_WEST, OUTER)
                .withProperty(SOUTH_EAST, OUTER)
                .withProperty(SOUTH_WEST, OUTER)
                .withProperty(TEXTURE, TextureType.BLANK));

        textures = new ArrayList<>();
        for (TextureType type: TextureType.values()) {
            textures.add(new WeightedTexture(type));
        }

        totalWeight = WeightedRandom.getTotalWeight(textures);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST, TEXTURE);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localizeFully(getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == 1) list.add(Text.translate("tooltip.dirt"));
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
        return world.getBlockState(pos).getBlock() == this;
    }

    private TextureStyle getStateFromBoolean(boolean one, boolean two, boolean three, boolean winter) {
        if (one && !two && !three) return VERTICAL;
        if (!one && two && !three) return  HORIZONTAL;
        if (one && two && !three) return INNER;
        if (!one && two) return HORIZONTAL;
        if (one && !two) return VERTICAL;
        if (one) return BLANK;
        return OUTER;
    }

    private TextureType getStyleFromPos(long rand) {
        return (WeightedRandom.getRandomItem(textures, Math.abs((int)rand >> 16) % totalWeight)).type;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = isSameBlock(world, pos.north());
        boolean west = isSameBlock(world, pos.west());
        boolean south = isSameBlock(world, pos.south());
        boolean east = isSameBlock(world, pos.east());
        boolean northEast = north && east && isSameBlock(world, pos.north().east());
        boolean northWest = north && west && isSameBlock(world, pos.north().west());
        boolean southEast = south && east && isSameBlock(world, pos.south().east());
        boolean southWest = south && west && isSameBlock(world, pos.south().west());
        boolean winter = HFTrackers.getCalendar(WorldHelper.getWorld(world)).getSeasonAt(pos) == Season.WINTER;
        TextureStyle ne = getStateFromBoolean(north, east, northEast, winter);
        TextureStyle nw = getStateFromBoolean(north, west, northWest, winter);
        TextureStyle se = getStateFromBoolean(south, east, southEast, winter);
        TextureStyle sw = getStateFromBoolean(south, west, southWest, winter);
        state = state.withProperty(NORTH_EAST, ne);
        state = state.withProperty(NORTH_WEST, nw);
        state = state.withProperty(SOUTH_EAST, se);
        state = state.withProperty(SOUTH_WEST, sw);
        state = state.withProperty(TEXTURE, getStyleFromPos(MathHelper.getPositionRandom(pos)));
        return state;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "ne=outer,nw=outer,se=outer,sw=outer,texture=blank"));
    }
}
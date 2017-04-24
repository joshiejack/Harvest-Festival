package joshie.harvest.crops.block;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import joshie.harvest.crops.tile.TileFruit;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BlockFruit extends BlockHFEnum<BlockFruit, Fruit> {
    @SuppressWarnings("ConstantConditions")
    public BlockFruit() {
        super(Material.PLANTS, Fruit.class);
        setSoundType(SoundType.PLANT);
        setCreativeTab(null);
    }

    public enum Fruit implements IStringSerializable {
        APPLE, BANANA, GRAPE, ORANGE, PEACH;

        private final ResourceLocation cropLocation;
        private Crop crop;

        Fruit() {
            this.cropLocation = new ResourceLocation(MODID, getName());
        }

        public Crop getCrop() {
            if (crop != null) return crop;
            else {
                crop = Crop.REGISTRY.get(cropLocation);
                return crop;
            }
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Fruit fruit = getEnumFromState(state);
        world.setBlockToAir(pos);
        spawnAsEntity(world, pos, fruit.getCrop().getCropStack(1));
        return true;
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        Fruit fruit = getEnumFromState(state);
        ret.add(fruit.getCrop().getCropStack(1));
        return ret;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFruit();
    }
}
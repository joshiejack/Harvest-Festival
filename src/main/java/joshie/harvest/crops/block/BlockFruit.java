package joshie.harvest.crops.block;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BlockFruit extends BlockHFEnum<BlockFruit, Fruit> {
    public BlockFruit() {
        super(Material.PLANTS, Fruit.class);
        setSoundType(SoundType.PLANT);
    }

    public enum Fruit implements IStringSerializable {
        APPLE(false), APPLE_HARVESTABLE(true),
        BANANA(false), BANANA_HARVESTABLE(true),
        GRAPE(false), GRAPE_HARVESTABLE(true),
        ORANGE(false), ORANGE_HARVESTABLE(true),
        PEACH(false), PEACH_HARVESTABLE(true);

        private final boolean harvestable;
        private final ResourceLocation cropLocation;
        private Crop crop;

        Fruit(boolean harvestable) {
            String name = getName().replace("_harvestable", "");
            this.cropLocation = new ResourceLocation(MODID, name);
            this.harvestable = harvestable;
        }

        public Crop getCrop() {
            if (crop != null) return crop;
            else {
                crop = Crop.REGISTRY.getValue(cropLocation);
                return crop;
            }
        }

        public boolean isHarvestable() {
            return harvestable;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Fruit fruit = getEnumFromState(state);
        if (fruit.isHarvestable()) {
            world.setBlockState(pos, getStateFromMeta(fruit.ordinal() - 1));
            spawnAsEntity(world, pos, fruit.getCrop().getCropStack(1));
            return true;
        }

        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        Fruit fruit = getEnumFromState(state);
        if (fruit.isHarvestable()) {
            ret.add(fruit.getCrop().getCropStack(1));
        }

        return ret;
    }
}
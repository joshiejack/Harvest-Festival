package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockBaseCrop extends BlockCrops {
    public static final int LESS_SEEDS = -33397;
    protected static PropertyInteger temporary;
    protected PropertyInteger stages;
    private final int maxStage;

    public BlockBaseCrop(ResourceLocation registry, int num) {
        this(registry, preInit(num), true);
    }
    @SuppressWarnings("unused")
    private BlockBaseCrop(ResourceLocation registry, int num, boolean ignored) {
        stages =  PropertyInteger.create("stage", 0, num - 1);
        maxStage = num - 1;
        stages = temporary;
        setDefaultState(blockState.getBaseState());
        RegistryHelper.registerBlock(registry, this);
    }

    private static int preInit(int num) {
        temporary = PropertyInteger.create("stage", 0, num - 1);
        return num;
    }

    @Nonnull
    @Override
    public PropertyInteger getAgeProperty() {
        return stages == null? temporary: stages;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if(stages == null) return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, stages);
    }

    @Override
    public int getMaxAge() {
        return maxStage;
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return super.getBonemealAgeIncrease(worldIn) / maxStage;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        ItemStack seeds = getSeedStack();
        if (fortune != LESS_SEEDS && !seeds.isEmpty()) drops.add(seeds); //Add a stack of seeds by default
        int age = getAge(state);
        if (age >= getMaxAge())  {
            Item item = this.getItemDropped(state, rand, fortune);
            if (item != Items.AIR)  {
                drops.add(getCropStack());
            }

            if (fortune != LESS_SEEDS && !seeds.isEmpty()) {
                //Allow for some additional seeds to drop when grown
                for (int i = 0; i < 2 + fortune; ++i) {
                    if (rand.nextInt(2 * getMaxAge()) <= age) {
                        drops.add(getSeedStack());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected abstract ItemStack getCropStack();
    protected abstract ItemStack getSeedStack();
}


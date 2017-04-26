package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalAction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class EntityAIEatLivestock extends EntityAIEat {
    public EntityAIEatLivestock(EntityAnimal animal) {
        super(animal);
    }

    @Override
    boolean isEdible(BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.TALLGRASS || (block == Blocks.TALLGRASS && state.getValue(BlockDoublePlant.VARIANT) == EnumPlantType.GRASS)) {
            return animal.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 1D;
        }

        return super.isEdible(pos, state);
    }

    protected void eat(BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.TALLGRASS) {
            getStats().performAction(animal.world, ItemStack.EMPTY, AnimalAction.FEED);
            animal.world.setBlockToAir(pos);
        } else if (block == Blocks.TALLGRASS && state.getValue(BlockDoublePlant.VARIANT) == EnumPlantType.GRASS) {
            getStats().performAction(animal.world, ItemStack.EMPTY, AnimalAction.FEED);
            animal.world.setBlockToAir(pos);
            if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.LOWER) {
                animal.world.setBlockToAir(pos.up());
            } else animal.world.setBlockToAir(pos.down());
        } else super.eat(pos, state);
    }
}
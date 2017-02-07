package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.animals.AnimalAction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
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
            getStats().performAction(animal.worldObj, null, AnimalAction.FEED);
            animal.worldObj.setBlockToAir(pos);
        } else if (block == Blocks.TALLGRASS && state.getValue(BlockDoublePlant.VARIANT) == EnumPlantType.GRASS) {
            getStats().performAction(animal.worldObj, null, AnimalAction.FEED);
            animal.worldObj.setBlockToAir(pos);
            if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.LOWER) {
                animal.worldObj.setBlockToAir(pos.up());
            } else animal.worldObj.setBlockToAir(pos.down());
        } else super.eat(pos, state);
    }

    /**
    @Override
    protected boolean attemptToEat(BlockPos position, IBlockState state, Block block) {
        if (block == Blocks.TALLGRASS) {
            getStats().performAction(animal.worldObj, null, null, AnimalAction.FEED);
            animal.worldObj.setBlockToAir(position);
            return true;
        } else if (block == Blocks.TALLGRASS && state.getValue(BlockDoublePlant.VARIANT) == EnumPlantType.GRASS) {
            getStats().performAction(animal.worldObj, null, null, AnimalAction.FEED);
            animal.worldObj.setBlockToAir(position);
            if (state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.LOWER) {
                animal.worldObj.setBlockToAir(position.up());
            } else animal.worldObj.setBlockToAir(position.down());

            return true;
        } else return super.attemptToEat(position, state, block);
    } **/
}
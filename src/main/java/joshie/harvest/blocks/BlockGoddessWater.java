package joshie.harvest.blocks;

import joshie.harvest.core.helpers.generic.RegistryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class BlockGoddessWater extends BlockFluidClassic {
    public BlockGoddessWater(Fluid fluid) {
        super(fluid, Material.WATER);
        quantaPerBlock = 8;
        quantaPerBlockFloat = 8;
    }

    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return null;
    }

    private int tick;

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);
        tick++;

        if (tick % 15 == 0 && !world.isRemote && world instanceof WorldServer) {
            //TODO: Accept items to summon goddess
        }
    }

    @Override
    public BlockGoddessWater setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerBlock(this, name);
        return this;
    }
}
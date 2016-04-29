package joshie.harvest.blocks;

import joshie.harvest.core.util.generic.IHasMetaBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        String register = name.replace(".", "_");
        if (this instanceof IHasMetaBlock) {
            Class<? extends ItemBlock> clazz = ((IHasMetaBlock) this).getItemClass();
            if (clazz == null) {
                String pack = this.getClass().getPackage().getName() + ".items.";
                String thiz = "Item" + this.getClass().getSimpleName();
                try {
                    clazz = (Class<? extends ItemBlock>) Class.forName(pack + thiz);
                } catch (Exception ignored) {
                }
            }

            GameRegistry.registerBlock(this, clazz, register);
        } else GameRegistry.registerBlock(this, register);

        return this;
    }
}
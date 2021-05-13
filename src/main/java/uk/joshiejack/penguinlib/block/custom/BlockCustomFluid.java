package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.block.base.BlockFluidBase;
import uk.joshiejack.penguinlib.scripting.Scripting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockCustomFluid extends BlockFluidBase {
    private ResourceLocation script;

    public BlockCustomFluid(Fluid fluid, String modid, ResourceLocation script) {
        super(fluid, modid, null);
        this.script = script;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        Scripting.callFunction(script, "onEntityCollision", world, pos, state, entity);
    }
}

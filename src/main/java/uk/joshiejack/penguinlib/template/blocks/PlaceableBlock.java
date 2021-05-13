package uk.joshiejack.penguinlib.template.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;

@PenguinLoader("block")
public class PlaceableBlock extends Placeable {
    protected transient IBlockState theState;
    protected String state = StateAdapter.toString(Blocks.AIR.getDefaultState());
    protected boolean interactable;

    public PlaceableBlock() {}
    public PlaceableBlock(IBlockState state, BlockPos pos) {
        this.theState = state;
        this.state = StateAdapter.toString(state);
        this.pos = pos;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public Block getBlock() {
        return getState().getBlock();
    }

    public IBlockState getState() {
        if (theState == null) theState = StateAdapter.fromString(state);
        return theState;
    }

    public IBlockState getTransformedState(Rotation rotation) {
        return getState().withRotation(rotation);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    private boolean prePlace(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlockHardness(world, pos) != -1.0F;
    }

    @Override
    protected final boolean place(World world, BlockPos pos, Rotation rotation, boolean playSound) {
        if (!prePlace(world, pos)) return false;
        IBlockState state = getTransformedState(rotation);
        if (world.getBlockState(pos) == state) return false;
        if (playSound) {
            SoundType soundtype = state.getBlock().getSoundType(state, world, pos, null);
            world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        }

        boolean result = world.setBlockState(pos, state, 2);
        if (result) {
            postPlace(world, pos, rotation);
        }

        return state.getBlock() != Blocks.AIR && result;
    }

    @Override
    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState state) {
        if (canPlace(stage)) {
            BlockPos transformed = BlockPosHelper.getTransformedPosition(this.pos, pos, rotation);
            world.setBlockState(transformed, state);
        }
    }

    public void postPlace(World world, BlockPos pos, Rotation rotation) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if ((!(obj instanceof PlaceableBlock))) return false;
        PlaceableBlock other = (PlaceableBlock) obj;
        return getX() == other.getX() && getY() == other.getY() && (getZ() == other.getZ());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + getX();
        result = prime * result + getY();
        result = prime * result + getZ();
        return result;
    }
}
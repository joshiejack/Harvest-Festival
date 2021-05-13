package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("chest")
public class PlaceableChest extends PlaceableBlock {
    private ResourceLocation chestType;

    @SuppressWarnings("unused")
    public PlaceableChest() {}
    public PlaceableChest(String name, IBlockState state, BlockPos position) {
        super(state, position);
        this.chestType = new ResourceLocation(name);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (chestType != null && tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLootTable(chestType, world.rand.nextLong());
        }
    }
}
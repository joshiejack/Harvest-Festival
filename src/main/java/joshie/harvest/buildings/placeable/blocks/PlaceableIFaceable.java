package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.core.util.interfaces.IFaceable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableIFaceable extends PlaceableBlock {
    @Expose
    private EnumFacing facing;
    @Expose
    private ResourceLocation chestType;

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            EnumFacing orientation = rotation.rotate(this.facing);
            ((IFaceable) tile).setFacing(orientation);
        }
    }
}
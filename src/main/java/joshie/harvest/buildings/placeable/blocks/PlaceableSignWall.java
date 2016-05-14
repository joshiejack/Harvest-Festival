package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PlaceableSignWall extends PlaceableBlock {
    private TextComponentString[] text;

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace (World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            //((TileEntitySign) tile).signText = text; //TODO: Fix signs
        }
    }
}
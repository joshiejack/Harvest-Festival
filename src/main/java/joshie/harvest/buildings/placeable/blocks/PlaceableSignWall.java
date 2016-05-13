package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PlaceableSignWall extends PlaceableBlock {
    private TextComponentString[] text;

    public PlaceableSignWall(Block block, int meta, BlockPos offsetPos, String... text) {
        super(block, meta, offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
        this.text = new TextComponentString[]{new TextComponentString(text[0]), new TextComponentString(text[1]), new TextComponentString(text[2]), new TextComponentString(text[3])};
    }

    public PlaceableSignWall(Block block, int meta, int offsetX, int offsetY, int offsetZ, String... text) {
        this(block, meta, new BlockPos(offsetX, offsetY, offsetZ), text);
    }

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
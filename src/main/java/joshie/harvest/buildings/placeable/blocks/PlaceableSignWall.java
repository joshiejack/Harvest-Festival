package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableSignWall extends PlaceableBlock {
    private ITextComponent[] text;

    public PlaceableSignWall(Block block, int meta, BlockPos offsetPos, String... text) {
        super(block, meta, offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
        this.text = new ITextComponent[]{new TextComponentString(text[1]), new TextComponentString(text[2]), new TextComponentString(text[3]), new TextComponentString(text[4])};
    }

    public PlaceableSignWall(Block block, int meta, int offsetX, int offsetY, int offsetZ, String... text) {
        this(block, meta, new BlockPos(offsetX, offsetY, offsetZ), text);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            text = ((TileEntitySign) tile).signText;
        }
    }
}
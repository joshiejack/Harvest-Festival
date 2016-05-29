package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PlaceableSign extends PlaceableDecorative {
    private TextComponentString[] text;

    public PlaceableSign() {}
    public PlaceableSign(IBlockState state, int x, int y, int z, ITextComponent... text) {
        super(state, x, y, z);
        this.text = new TextComponentString[text.length];
        for (int i = 0; i < text.length; i++) {
            this.text[i] = (TextComponentString) text[i];
        }
    }

    @Override
    public void postPlace (World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            //((TileEntitySign) tile).signText = text; //TODO: Fix signs
        }
    }
}
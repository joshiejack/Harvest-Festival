package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class PlaceableSign extends PlaceableDecorative {
    @Expose
    private ITextComponent[] text;

    public PlaceableSign() {}
    public PlaceableSign(IBlockState state, int x, int y, int z, ITextComponent... text) {
        super(state, x, y, z);
        this.text = new ITextComponent[text.length];
        for (int i = 0; i < text.length; i++) {
            this.text[i] = text[i];
        }
    }

    @Override
    public void postPlace (World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            for (int i = 0; i < 4; ++i) {
                ((TileEntitySign) tile).signText[i] = text[i];
            }
        }
    }
}
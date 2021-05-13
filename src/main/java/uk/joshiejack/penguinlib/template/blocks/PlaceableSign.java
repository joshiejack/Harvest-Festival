package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

@PenguinLoader("sign")
public class PlaceableSign extends PlaceableDecorative {
    private ITextComponent[] text;

    @SuppressWarnings("unused")
    public PlaceableSign() {}
    public PlaceableSign(IBlockState state, BlockPos position, ITextComponent... text) {
        super(state, position);
        this.text = new ITextComponent[text.length];
        System.arraycopy(text, 0, this.text, 0, text.length);
    }

    @Override
    public void postPlace (World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            System.arraycopy(text, 0, ((TileEntitySign) tile).signText, 0, 4);
        }
    }
}
package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableSignWall extends PlaceableBlock {
    private ITextComponent[] text;

    public PlaceableSignWall(Block block, int meta, BlockPos offsetPos, String... text) {
        super(block, meta, offsetPos);
        this.text = new ITextComponent[]{new TextComponentString(text[1]), new TextComponentString(text[2]), new TextComponentString(text[3]), new TextComponentString(text[4])};
    }

    public PlaceableSignWall(Block block, int meta, int offsetX, int offsetY, int offsetZ, String... text) {
        this(block, meta, new BlockPos(offsetX, offsetY, offsetZ), text);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 2) {
            if (n2) {
                return swap ? 5 : 3;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 3 : 5;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 3;
            }
        }

        return meta;
    }

    @Override
    public boolean place(UUID uuid, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap) {
        if (!super.place(uuid, world, pos, state, n1, n2, swap)) return false;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntitySign) {
            text = ((TileEntitySign) tile).signText;
            return true;
        } else return false;
    }
}
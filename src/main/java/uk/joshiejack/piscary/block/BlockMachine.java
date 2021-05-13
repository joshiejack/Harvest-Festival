package uk.joshiejack.piscary.block;

import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import uk.joshiejack.piscary.Piscary;
import uk.joshiejack.piscary.tile.TileRecyler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockMachine extends BlockMultiTileRotatable<BlockMachine.Machine> {
    public BlockMachine() {
        super(new ResourceLocation(Piscary.MODID, "machine"), Material.PISTON, Machine.class);
        setCreativeTab(Piscary.TAB);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileRecyler();
    }

    public enum Machine implements IStringSerializable {
        RECYCLER;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

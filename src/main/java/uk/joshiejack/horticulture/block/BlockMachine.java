package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.tileentity.TileSeedMaker;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatableDouble;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockMachine extends BlockMultiTileRotatableDouble<BlockMachine.Machine> {
    public BlockMachine() {
        super(new ResourceLocation(MODID, "machine"), Material.PISTON, Machine.class);
        setHardness(3F);
        setCreativeTab(Horticulture.TAB);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileSeedMaker();
    }

    public enum Machine implements IStringSerializable {
        SEED_MAKER;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

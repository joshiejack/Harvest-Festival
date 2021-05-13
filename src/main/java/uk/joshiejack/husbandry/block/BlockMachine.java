package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.tile.TileHive;
import uk.joshiejack.husbandry.tile.TileOilMaker;
import uk.joshiejack.husbandry.tile.TileSpinningWheel;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class BlockMachine extends BlockMultiTileRotatable<BlockMachine.Machine> {
    public BlockMachine() {
        super(new ResourceLocation(MODID, "machine"), Material.PISTON, Machine.class);
        setCreativeTab(Husbandry.TAB);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case OIL_MAKER:
                return new TileOilMaker();
            case HIVE:
                return new TileHive();
            case SPINNING_WHEEL:
            default:
                return new TileSpinningWheel();
        }
    }

    public enum Machine implements IStringSerializable {
        SPINNING_WHEEL, OIL_MAKER, HIVE;

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

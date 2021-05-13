package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.tile.TileFermenter;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatableDouble;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class BlockDoubleMachine extends BlockMultiTileRotatableDouble<BlockDoubleMachine.Machine> {
    public BlockDoubleMachine() {
        super(new ResourceLocation(MODID, "double_machine"), Material.PISTON, Machine.class);
        setCreativeTab(Husbandry.TAB);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFermenter();
    }

    public enum Machine implements IStringSerializable {
        FERMENTER;

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

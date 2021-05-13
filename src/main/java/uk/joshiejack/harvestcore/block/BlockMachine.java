package uk.joshiejack.harvestcore.block;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.tile.TileFurnace;
import uk.joshiejack.harvestcore.tile.TileKiln;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatableDouble;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockMachine extends BlockMultiTileRotatableDouble<BlockMachine.Machine> {
    public BlockMachine() {
        super(new ResourceLocation(HarvestCore.MODID, "machine"), Material.PISTON, Machine.class);
        setHardness(3F);
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return getEnumFromState(state) == Machine.FURNACE ? new TileFurnace() : new TileKiln();
    }

    public enum Machine implements IStringSerializable {
        FURNACE, KILN;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

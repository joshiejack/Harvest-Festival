package joshie.harvest.animals.render;

import joshie.harvest.animals.block.BlockTrough.Section;
import joshie.harvest.animals.tile.TileTrough;
import joshie.harvest.core.base.render.TileSpecialRendererItem;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class SpecialRendererTrough extends TileSpecialRendererItem<TileTrough> {
    @Override
    public void renderTileEntityAt(@Nonnull TileTrough tile, double x, double y, double z, float tick, int destroyStage) {
        TileTrough master = tile.getMaster();
        if (master.getFillAmount() > 0) {
            float height = 0.25F + ((float) master.getFillAmount() / (40 * master.getSize())) * 0.45F;
            Section section = tile.getSection();
            if (section == Section.SINGLE) {
                renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.5F, (float) y + height, (float) z + 0.5F, 0.75F, 0.75F);
            } else if (section == Section.END) {
                EnumFacing facing = tile.getFacing();
                if (facing == EnumFacing.NORTH) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.45F, (float) y + height, (float) z + 0.5F, 0.9F, 0.75F);
                } else if (facing == EnumFacing.SOUTH) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.55F, (float) y + height, (float) z + 0.5F, 0.9F, 0.75F);
                } else if (facing == EnumFacing.EAST) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.5F, (float) y + height, (float) z + 0.45F, 0.75F, 0.9F);
                } else if (facing == EnumFacing.WEST) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.5F, (float) y + height, (float) z + 0.55F, 0.75F, 0.9F);
                }
            } else if (section == Section.MIDDLE) {
                EnumFacing facing = tile.getFacing();
                if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.5F, (float) y + height, (float) z + 0.5F, 1F, 0.75F);
                } else if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    renderFluidPlane(AnimalMappingEvent.FODDER, (float) x + 0.5F, (float) y + height, (float) z + 0.5F, 0.75F, 1F);
                }
            }
        }
    }
}

package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockBedBase;
import net.minecraft.util.ResourceLocation;

public class BlockChildBed extends BlockBedBase {
    public BlockChildBed() {
        super(new ResourceLocation(Furniture.MODID, "child_bed"));
        setCreativeTab(Furniture.INSTANCE);
    }
}

package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockBedBase;
import net.minecraft.util.ResourceLocation;

public class BlockDoubleBed extends BlockBedBase {
    public BlockDoubleBed() {
        super(new ResourceLocation(Furniture.MODID, "double_bed"));
        setCreativeTab(Furniture.INSTANCE);
    }
}

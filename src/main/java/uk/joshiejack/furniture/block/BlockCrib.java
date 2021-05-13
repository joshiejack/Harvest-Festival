package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockBedBase;
import net.minecraft.util.ResourceLocation;

public class BlockCrib extends BlockBedBase {
    public BlockCrib() {
        super(new ResourceLocation(Furniture.MODID, "crib"));
        setCreativeTab(Furniture.INSTANCE);
    }
}

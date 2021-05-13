package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockLadderBase;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class BlockOldLadder extends BlockLadderBase<BlockOldLadder.Ladder> {
    public BlockOldLadder() {
        super(new ResourceLocation(Furniture.MODID, "ladder"), Ladder.class);
        setCreativeTab(Furniture.INSTANCE);
    }

    public enum Ladder implements IStringSerializable {
        OAK, SPRUCE, BIRCH;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

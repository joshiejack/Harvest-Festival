package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockLadderBase;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class BlockNewLadder extends BlockLadderBase<BlockNewLadder.Ladder> {
    public BlockNewLadder() {
        super(new ResourceLocation(Furniture.MODID, "ladder2"), Ladder.class);
        setCreativeTab(Furniture.INSTANCE);
    }

    public enum Ladder implements IStringSerializable {
        JUNGLE, ACACIA, DARK_OAK;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

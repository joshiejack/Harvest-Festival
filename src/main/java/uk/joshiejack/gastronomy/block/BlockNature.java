package uk.joshiejack.gastronomy.block;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.block.base.BlockMultiBush;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class BlockNature extends BlockMultiBush<BlockNature.Nature> {
    public BlockNature() {
        super(new ResourceLocation(MODID, "nature"), Nature.class);
        setCreativeTab(Gastronomy.TAB);
    }

    public enum Nature implements IStringSerializable {
        MATSUTAKE, BAMBOO, MINT, CHAMOMILE, LAVENDER;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

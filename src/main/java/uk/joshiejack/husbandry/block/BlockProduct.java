package uk.joshiejack.husbandry.block;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.block.base.BlockMulti;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class BlockProduct extends BlockMulti<BlockProduct.Product> {
    public BlockProduct() {
        super(new ResourceLocation(MODID, "product"), Material.CLOTH, Product.class);
        setHardness(0.25F);
        setSoundType(SoundType.CLOTH);
        setCreativeTab(Husbandry.TAB);
    }

    public enum Product implements IStringSerializable {
        SMALL_TRUFFLE, MEDIUM_TRUFFLE, LARGE_TRUFFLE,
        SMALL_RABBIT_WOOL, MEDIUM_RABBIT_WOOL, LARGE_RABBIT_WOOL;

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

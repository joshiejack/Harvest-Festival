package uk.joshiejack.furniture.block;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class BlockInteractable extends BlockMultiTileRotatable<BlockInteractable.Type> {
    public BlockInteractable() {
        super(new ResourceLocation(Furniture.MODID), Material.WOOD, Type.class);
        setHardness(1.5F);
        setSoundType(SoundType.WOOD);
        setCreativeTab(Furniture.INSTANCE);
    }

    public enum Type implements IStringSerializable {
        COT;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

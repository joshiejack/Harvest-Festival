package uk.joshiejack.penguinlib.util;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyBlockState implements IUnlistedProperty<IBlockState> {
    public static final PropertyBlockState INSTANCE = new PropertyBlockState();

    @Override
    public String getName() {
        return "PropertyBlockState";
    }

    @Override
    public boolean isValid(IBlockState value) {
        return true;
    }

    @Override
    public Class<IBlockState> getType() {
        return IBlockState.class;
    }

    @Override
    public String valueToString(IBlockState value) {
        return value.toString();
    }
}

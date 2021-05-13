package uk.joshiejack.furniture.block.properties;

import net.minecraftforge.common.property.IUnlistedProperty;
import uk.joshiejack.furniture.television.TVProgram;

public class PropertyTVProgram implements IUnlistedProperty<TVProgram> {
    public static final PropertyTVProgram INSTANCE = new PropertyTVProgram();

    @Override
    public String getName() {
        return "PropertyTVProgram";
    }

    @Override
    public boolean isValid(TVProgram value) {
        return true;
    }

    @Override
    public Class<TVProgram> getType() {
        return TVProgram.class;
    }

    @Override
    public String valueToString(TVProgram value) {
        return value.getRegistryName().toString();
    }
}

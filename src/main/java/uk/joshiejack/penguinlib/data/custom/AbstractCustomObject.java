package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("rawtypes")
public abstract class AbstractCustomObject {
    public static final BiMap<String, AbstractCustomData> TYPE_REGISTRY = HashBiMap.create();
    public ResourceLocation registryName;
    public String type;

    public static class Multi extends AbstractCustomObject {
        public AbstractCustomData defaults;
        public AbstractCustomData[] data;
    }

    public static class Singular extends AbstractCustomObject {
        public AbstractCustomData data;
    }
}

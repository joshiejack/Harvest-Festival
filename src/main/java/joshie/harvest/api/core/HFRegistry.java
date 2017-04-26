package joshie.harvest.api.core;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

public abstract class HFRegistry<T extends HFRegistry<T>> {
    private final ResourceLocation resource;

    @SuppressWarnings("unchecked")
    public HFRegistry(ResourceLocation resource) {
        this.resource = resource;
        getRegistry().put(resource, (T) this);
    }

    public abstract Map<ResourceLocation, T> getRegistry();

    public ResourceLocation getResource() {
        return resource;
    }
}
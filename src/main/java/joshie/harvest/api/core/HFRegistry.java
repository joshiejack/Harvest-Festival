package joshie.harvest.api.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.util.Map;

//TODO: Remove the underlying forge registry in 0.7+
//public abstract class HFRegistry<T  extends IForgeRegistryEntry<T>> extends IForgeRegistryEntry.Impl<T> {
//public abstract class HFRegistry<T extends HFRegistry<T>> {
public abstract class HFRegistry<T  extends IForgeRegistryEntry<T>> extends IForgeRegistryEntry.Impl<T> {
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

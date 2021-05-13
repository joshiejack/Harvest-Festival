package uk.joshiejack.harvestcore.registry;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Objects;

public class Quality extends PenguinRegistry {
    public static final Map<ResourceLocation, Quality> REGISTRY = Maps.newLinkedHashMap(); //Keep the order?

    private final double modifier;
    private ResourceLocation model;

    public Quality(ResourceLocation name, double modifier) {
        super(REGISTRY, name);
        this.modifier = modifier;
        this.model = new ModelResourceLocation(new ResourceLocation(name.getNamespace(), "quality"), name.getPath());
    }

    public void setModel(String model) {
        this.model = new ResourceLocation(model);
    }

    public double modifier() {
        return modifier;
    }

    public ResourceLocation model() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quality quality = (Quality) o;
        return Objects.equals(resource, quality.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
}

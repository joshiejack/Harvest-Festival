package uk.joshiejack.harvestcore.registry;

import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class Fertilizer extends PenguinRegistry.Item<Fertilizer> {
    public static final Map<ResourceLocation, Fertilizer> REGISTRY = Maps.newLinkedHashMap(); //Keep the order?
    public static final Fertilizer NONE = new Fertilizer(new ResourceLocation(HarvestCore.MODID, "none"), 0, 0);

    private final int speed;
    private final int quality;
    private final ResourceLocation blockSprite;

    private Fertilizer(ResourceLocation name, int speed, int quality) {
        super("fertilizer", REGISTRY, name);
        this.speed = speed;
        this.quality = quality;
        this.blockSprite = new ResourceLocation(name.getNamespace(), "blocks/fertilizer/" + name.getPath());
    }

    public static Fertilizer create(ResourceLocation name, int speed, int quality) {
        return new Fertilizer(name, speed, quality);
    }

    public int getSpeed() {
        return speed;
    }

    public int getQuality() {
        return quality;
    }

    public ResourceLocation getBlockSprite() {
        return blockSprite;
    }
}

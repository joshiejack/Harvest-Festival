package uk.joshiejack.furniture.television;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import java.util.Map;

public class TVProgram extends PenguinRegistry {
    public static final Map<ResourceLocation, TVProgram> REGISTRY = Maps.newHashMap();
    public static final TVProgram OFF = new TVProgram(new ResourceLocation(Furniture.MODID, "off"));
    private final String sprite;

    public static TVProgram create(ResourceLocation id) { return new TVProgram(id); }

    private TVProgram(ResourceLocation resource) {
        super(REGISTRY, resource);
        this.sprite = resource.getNamespace() + ":blocks/television/" + resource.getPath();
    }

    public String getTVSprite() {
        return sprite;
    }
}

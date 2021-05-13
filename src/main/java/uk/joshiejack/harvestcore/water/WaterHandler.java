package uk.joshiejack.harvestcore.water;

import com.google.common.collect.Maps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public abstract class WaterHandler {
    public static final Map<String, Builder> BUILDERS = Maps.newHashMap();
    public static final Map<String, WaterHandler> REGISTRY = Maps.newHashMap();
    protected final boolean match;
    private final String name;

    protected WaterHandler(String name, boolean match) {
        REGISTRY.put(name, this);
        this.name = name;
        this.match = match;
    }

    public abstract boolean isType(World world, BlockPos pos);

    public String name() {
        return name;
    }

    public abstract static class Builder {
        public abstract WaterHandler build(String name, String data);
    }
}

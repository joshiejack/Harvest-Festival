package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class HFSounds {
    public static final SoundEvent FRYING_PAN = getSoundEvent("harvestfestival:frying_pan");

    private static SoundEvent getSoundEvent(String name) {
        return SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
    }
}

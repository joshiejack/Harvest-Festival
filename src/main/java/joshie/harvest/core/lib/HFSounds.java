package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HFSounds {
    public static final SoundEvent FRYING_PAN = getSoundEvent("frying_pan");
    public static final SoundEvent MIXER = getSoundEvent("mixer");
    public static final SoundEvent SMASH_ROCK = getSoundEvent("smash_rock");
    public static final SoundEvent SMASH_WOOD = getSoundEvent("smash_wood");

    private static SoundEvent getSoundEvent(String name) {
        return SoundEvent.REGISTRY.getObject(new ResourceLocation(MODID, name));
    }
}

package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class HFSounds {
    public static final SoundEvent COUNTER = getSoundEvent("counter");
    public static final SoundEvent FRIDGE = getSoundEvent("fridge");
    public static final SoundEvent FRYING_PAN = getSoundEvent("frying_pan");
    public static final SoundEvent MIXER = getSoundEvent("mixer");
    public static final SoundEvent OVEN = getSoundEvent("oven");
    public static final SoundEvent OVEN_DONE = getSoundEvent("oven_done");
    public static final SoundEvent OVEN_DOOR = getSoundEvent("oven_door");
    public static final SoundEvent POT = getSoundEvent("pot");
    public static final SoundEvent SMASH_ROCK = getSoundEvent("smash_rock");
    public static final SoundEvent SMASH_WOOD = getSoundEvent("smash_wood");
    public static final SoundEvent BLESS_TOOL = getSoundEvent("blessing");
    public static final SoundEvent GODDESS_SPAWN = getSoundEvent("goddess");
    public static final SoundEvent RECIPE = getSoundEvent("recipe");
    public static final SoundEvent BRUSH = getSoundEvent("brush");

    private static SoundEvent getSoundEvent(String name) {
        return SoundEvent.REGISTRY.getObject(new ResourceLocation(MODID, name));
    }
}

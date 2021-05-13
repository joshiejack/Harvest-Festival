package uk.joshiejack.gastronomy;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;
import static uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper.createSoundEvent;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class GastronomySounds {
    public static final SoundEvent COUNTER = null;
    public static final SoundEvent FRIDGE = null;
    public static final SoundEvent FRYING_PAN = null;
    public static final SoundEvent MIXER = null;
    public static final SoundEvent OVEN = null;
    public static final SoundEvent OVEN_DONE = null;
    public static final SoundEvent OVEN_DOOR = null;
    public static final SoundEvent POT = null;
    public static final SoundEvent RECIPE = null;

    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(createSoundEvent(MODID, "counter"), createSoundEvent(MODID, "fridge"), createSoundEvent(MODID, "frying_pan"),
                                        createSoundEvent(MODID, "mixer"), createSoundEvent(MODID, "oven"), createSoundEvent(MODID, "oven_done"),
                                        createSoundEvent(MODID, "oven_door"), createSoundEvent(MODID, "pot"), createSoundEvent(MODID, "recipe"));
    }
}

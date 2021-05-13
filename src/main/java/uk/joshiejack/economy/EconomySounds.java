package uk.joshiejack.economy;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Economy.MODID)
@Mod.EventBusSubscriber(modid = Economy.MODID)
public class EconomySounds {
    public static final SoundEvent KERCHING = null;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(RegistryHelper.createSoundEvent(Economy.MODID, "kerching"));
    }
}

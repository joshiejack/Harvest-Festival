package uk.joshiejack.energy;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

@GameRegistry.ObjectHolder(Energy.MODID)
@Mod.EventBusSubscriber(modid = Energy.MODID)
public class EnergySounds {
    public static final SoundEvent YAWN = null;

    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(RegistryHelper.createSoundEvent(Energy.MODID, "yawn"));
    }
}

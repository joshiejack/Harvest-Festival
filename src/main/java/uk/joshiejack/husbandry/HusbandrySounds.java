package uk.joshiejack.husbandry;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class HusbandrySounds {
    public static final SoundEvent BRUSH = null;

    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(RegistryHelper.createSoundEvent(MODID, "brush"));
    }
}

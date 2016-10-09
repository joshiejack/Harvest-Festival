package joshie.harvest.plugins.morpheus;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.quetzi.morpheus.api.IMorpheusAPI;

@HFLoader(mods = "Morpheus")
public class Morpheus {
    public static void init() throws ClassNotFoundException {
        Class morpheus = Class.forName("net.quetzi.morpheus.Morpheus");
        IMorpheusAPI api = (IMorpheusAPI) ReflectionHelper.getPrivateValue(morpheus, null, "register");
        api.unregisterHandler(0); //Unregister first, so we don't get the warning
        api.registerHandler(new SleepHandlerOverworld(), 0); //Override the default morpheus handler
        if (HFCore.SLEEP_ANYTIME) { //If I'm allowing players to sleep anytime, override the morpheus config
            ReflectionHelper.setPrivateValue(morpheus, null, false, "setSpawnDaytime");
        }
    }
}

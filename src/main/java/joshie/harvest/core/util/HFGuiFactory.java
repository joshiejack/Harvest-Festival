package joshie.harvest.core.util;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.ConfigHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

@HFEvents(Side.CLIENT)
public class HFGuiFactory extends DefaultGuiFactory {

    protected HFGuiFactory() {
        super(MODID, MODNAME);
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiConfig(parentScreen, getConfigElements(), MODID, false, true, GuiConfig.getAbridgedConfigPath(ConfigHelper.getConfig().toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();

        List<Class> configsList = new ArrayList<>(HarvestFestival.proxy.getList());
        Collections.sort(configsList, Comparator.comparing(Class::getSimpleName));
        for (Class c : configsList) {
            try {
                Method configure = c.getMethod("configure");
                if (configure != null) {
                    String categoryName = c.getSimpleName().replace("HF", "");
                    List<IConfigElement> configElements = new ConfigElement(ConfigHelper.getConfig().getCategory(categoryName)).getChildElements();

                    list.add(new DummyConfigElement.DummyCategoryElement(categoryName, MODID + ".config", configElements));
                }

            } catch (Exception ignored) {
            }
        }
        return list;
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(MODID)) {
            Configuration config = ConfigHelper.getConfig();
            if (config.hasChanged()) {
                config.save();
            }

            //Reload in all the values
            HarvestFestival.proxy.configure();
            HFCaches.clearClient();
        }
    }
}
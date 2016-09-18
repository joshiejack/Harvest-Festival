package joshie.harvest.core.util;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static joshie.harvest.core.helpers.ConfigHelper.setConfig;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFEvents(Side.CLIENT)
public class HFGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiHFConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class GuiHFConfig extends GuiConfig {
        public GuiHFConfig(GuiScreen parentScreen) {
            super(parentScreen, getConfigElements(), MODID, true, true, GuiConfig.getAbridgedConfigPath(ConfigHelper.getConfig().toString()));
        }

        @SuppressWarnings("unchecked")
        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>();

            List<Class> configsList = new ArrayList<>(HarvestFestival.proxy.getList());
            Collections.sort(configsList, (c1, c2) -> c1.getSimpleName().compareTo(c2.getSimpleName()));
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
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(MODID)) {
            Configuration config = ConfigHelper.getConfig();

            ConfigHelper.setConfig(config);

            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
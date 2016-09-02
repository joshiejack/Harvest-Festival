package joshie.harvest.plugins.buttons;

import net.minecraft.util.ResourceLocation;
import tehnut.buttons.api.ButtonsPlugin;
import tehnut.buttons.api.IWidgetPlugin;
import tehnut.buttons.api.IWidgetRegistry;
import tehnut.buttons.api.WidgetTexture;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@ButtonsPlugin
public class Buttons extends IWidgetPlugin.Base {
    public static final ResourceLocation RESOURCE = new ResourceLocation(MODID, "textures/hud/gui_widgets.png");
    public static final WidgetTexture BLANK = new WidgetTexture(
            new ResourceLocation("buttons", "textures/gui_widgets.png"),
            0,
            0,
            0,
            0
    );

    @Override
    public void register(IWidgetRegistry registry) {
        registry.addUtilityButton(new ButtonSeason());
        registry.addUtilityButton(new ButtonWeather());
    }
}

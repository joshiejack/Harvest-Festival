package joshie.harvest.plugins.buttons;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.plugins.buttons.ButtonWeather.WeatherModes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tehnut.buttons.api.WidgetTexture;
import tehnut.buttons.api.button.utility.ButtonMode;
import tehnut.buttons.api.button.utility.IMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ButtonWeather extends ButtonMode<WeatherModes> {
    private final String name;

    public ButtonWeather() {
        super(Buttons.BLANK, WeatherModes.class);
        this.name = "button_weather";

        setServerRequired();
    }

    @Override
    public boolean requireElevatedPermissions() {
        return isServerRequired();
    }

    @Override
    public ResourceLocation getButtonId() {
        return new ResourceLocation("buttons", name);
    }

    public enum WeatherModes implements IMode {
        SUNNY(0, Weather.SUNNY),
        RAINY(20, Weather.RAIN),
        SNOWY(40, Weather.SNOW),
        TYPHOON(60, Weather.TYPHOON),
        BLIZZARD(80, Weather.BLIZZARD);

        private final WidgetTexture widgetTexture;
        private final Weather weather;

        WeatherModes(int x, Weather weather) {
            this.widgetTexture = new WidgetTexture(Buttons.RESOURCE, x, 40, 20, 20);
            this.weather = weather;
        }

        @Nonnull
        @Override
        public WidgetTexture getModeTexture() {
            return widgetTexture;
        }

        @Nullable
        @Override
        public List<? extends ITextComponent> getTooltip() {
            return Collections.singletonList(new TextComponentTranslation("harvestfestival.weather." + weather.name().toLowerCase(Locale.ENGLISH) + ".set"));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public EnumActionResult onClientClick(int mouseX, int mouseY) {
            return EnumActionResult.SUCCESS;
        }

        @Override
        public void onServerClick(EntityPlayerMP player) {
            HFTrackers.<CalendarServer>getCalendar(player.worldObj).setTodaysWeather(weather);
        }
    }
}

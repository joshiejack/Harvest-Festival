package joshie.harvest.plugins.buttons;

/* //TODO: Re-enable Buttons support
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
            HFTrackers.<CalendarServer>getCalendar(player.world).setTodaysWeather(weather);
        }
    }
} */

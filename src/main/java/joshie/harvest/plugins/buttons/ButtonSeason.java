package joshie.harvest.plugins.buttons;

/* //TODO: Re-enable Buttons support
public class ButtonSeason extends ButtonMode<HFSeason> {
    public ButtonSeason() {
        super(Buttons.BLANK, HFSeason.class);
        setServerRequired(); //Check the server
    }

    @Override
    public ResourceLocation getButtonId() {
        return new ResourceLocation(MODID, "seasons");
    }

    public enum HFSeason implements IMode {
        SPRING(0, Season.SPRING),
        SUMMER(20, Season.SUMMER),
        AUTUMN(40, Season.AUTUMN),
        WINTER(60, Season.WINTER),;

        private final WidgetTexture widgetTexture;
        private final Season season;

        HFSeason(int x, Season season) {
            this.widgetTexture = new WidgetTexture(Buttons.RESOURCE, x, 20, 20, 20);
            this.season = season;
        }

        @Nonnull
        @Override
        public WidgetTexture getModeTexture() {
            return widgetTexture;
        }

        @Nullable
        @Override
        public List<? extends ITextComponent> getTooltip() {
            return Collections.singletonList(new TextComponentTranslation("harvestfestival.season." + season.name().toLowerCase(Locale.ENGLISH)));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public EnumActionResult onClientClick(int mouseX, int mouseY) {
            return EnumActionResult.SUCCESS;
        }

        @Override
        public void onServerClick(EntityPlayerMP player) {
            World world = player.world;
            CalendarServer calendar = HFTrackers.getCalendar(world);
            int day = calendar.getDate().getDay();
            int year = Math.max(1, calendar.getDate().getYear());
            long leftover = player.world.getWorldTime() % HFCalendar.TICKS_PER_DAY;
            world.setWorldTime(CalendarHelper.getTime(day, season, year) + leftover);
            calendar.recalculateAndUpdate(world);
        }
    }
} */

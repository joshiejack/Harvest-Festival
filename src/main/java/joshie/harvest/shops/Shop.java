package joshie.harvest.shops;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.util.Text;
import joshie.harvest.shops.purchaseable.Purchaseable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop implements IShop {
    public static final List<IPurchaseable> allItems = new ArrayList<>();
    private List<IPurchaseable> contents = new ArrayList<>();
    private HashMultimap<EnumDifficulty, OpeningSettings> open = HashMultimap.create();
    private final ResourceLocation resourceLocation;
    private final String unlocalizedName;
    @SideOnly(Side.CLIENT)
    private IShopGuiOverlay overlay;

    public Shop(ResourceLocation resource) {
        resourceLocation = resource;
        unlocalizedName = resource.getResourceDomain() + ".shop." + resource.getResourcePath();
    }

    @Override
    public IShop addOpening(EnumDifficulty difficulty, Weekday day, int opening, int closing) {
        OpeningHours hours = new OpeningHours(opening, closing);
        OpeningSettings settings = new OpeningSettings();
        settings.opening.put(day, hours);
        open.get(difficulty).add(settings);
        return this;
    }

    @Override
    public IShop addOpening(Weekday day, int opening, int closing) {
        OpeningHours hard = new OpeningHours(opening, closing);
        OpeningHours normal = new OpeningHours(fix(opening - 3000), fix(closing + 2000));
        OpeningHours easy = new OpeningHours(fix(opening - 4000), fix(closing + 5000));
        OpeningSettings hSettings = new OpeningSettings();
        OpeningSettings nSettings = new OpeningSettings();
        OpeningSettings eSettings = new OpeningSettings();
        hSettings.opening.put(day, hard);
        nSettings.opening.put(day, normal);
        eSettings.opening.put(day, easy);
        open.get(EnumDifficulty.HARD).add(hSettings);
        open.get(EnumDifficulty.NORMAL).add(nSettings);
        open.get(EnumDifficulty.EASY).add(eSettings);
        return this;
    }

    @Override
    public IShop addItem(IPurchaseable item) {
        if (item != null) {
            this.contents.add(item);
            allItems.add(item);
        }

        return this;
    }

    @Override
    public IShop addItem(long cost, ItemStack... items) {
        return addItem(new Purchaseable(cost, items));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IShop setGuiOverlay(IShopGuiOverlay overlay) {
        this.overlay = overlay;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public IShopGuiOverlay getGuiOverlay() {
        return overlay;
    }

    public String getLocalizedName() {
        return Text.localize(unlocalizedName);
    }

    public String getWelcome() {
        return Text.getRandomSpeech(resourceLocation, unlocalizedName + ".greeting", 10);
    }

    public List<IPurchaseable> getContents(@Nonnull EntityPlayer player) {
        List<IPurchaseable> contents = new ArrayList<>();
        for (IPurchaseable purchaseable : this.contents) {
            if (purchaseable.canList(player.worldObj, player)) {
                contents.add(purchaseable);
            }
        }

        return contents;
    }

    public boolean isOpen(World world, @Nullable EntityPlayer player) {
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) return true;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningSettings settings: open.get(world.getDifficulty())) {
            OpeningHours hours = settings.opening.get(day);
            if (hours != null) {
                long daytime = CalendarHelper.getTime(world); //0-23999 by default
                int scaledOpening = CalendarHelper.getScaledTime(hours.open);
                int scaledClosing = CalendarHelper.getScaledTime(hours.close);
                boolean isOpen = daytime >= scaledOpening && daytime <= scaledClosing;
                if (isOpen && (player == null || getContents(player).size() > 0)) return true;
            }
        }

        return false;
    }

    public boolean isPreparingToOpen(World world) {
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) return false;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningSettings settings: open.get(world.getDifficulty())) {
            OpeningHours hours = settings.opening.get(day);
            if (hours != null) {
                long daytime = CalendarHelper.getTime(world); //0-23999 by default
                int hourHalfBeforeWork = fix(CalendarHelper.getScaledTime(hours.open) - 1500);
                if(daytime >= hourHalfBeforeWork) return true;
            }
        }

        return false;
    }

    private int fix(int i) {
        return Math.min(24000, Math.max(0, i));
    }

    private static class OpeningSettings {
        private HashMap<Weekday, OpeningHours> opening = new HashMap<>();
    }

    /** The integers in here are as follows
     * 1000 = 1 AM
     * 2500 = 2:30am
     * 18000 = 6PM
     * etc. */
    private static class OpeningHours {
        private int open;
        private int close;

        OpeningHours(int open, int close) {
            this.open = open;
            this.close = close;
        }
    }
}
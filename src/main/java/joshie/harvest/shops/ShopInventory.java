package joshie.harvest.shops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import joshie.harvest.api.core.Weekday;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ShopInventory implements IShop {
    public static final ArrayList<IPurchaseable> registers = new ArrayList();
    private ArrayList<IPurchaseable> contents = new ArrayList();
    protected ArrayList<String> greetings = new ArrayList();
    private HashMap<EnumDifficulty, OpeningSettings> open = new HashMap();
    private String name;
    protected int last;

    public ShopInventory(String name) {
        for (int i = 1; i < 32; i++) {
            String key = HFModInfo.MODPATH + ".shop." + name + ".greeting" + i;
            String greeting = Text.localize(key);
            if (!greeting.equals(key)) {
                greetings.add(greeting);
            }
        }

        Collections.shuffle(greetings);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            overlay = new ShopInventoryGui(0);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IShopGuiOverlay overlay;
    
    @SideOnly(Side.CLIENT)
    public IShop setGuiOverlay(IShopGuiOverlay overlay) {
        this.overlay = overlay;
        return this;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IShopGuiOverlay getGuiOverlay() {
        return overlay;
    }
    
    /** Returns the location of the shops name **/
    public ResourceLocation getResource() {
        ResourceLocation shop_texture = new ResourceLocation(HFModInfo.MODPATH + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/shops.png");
        try {
            MCClientHelper.getMinecraft().renderEngine.getTexture(shop_texture).loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (Exception e) {
            shop_texture = new ResourceLocation(HFModInfo.MODPATH + ":lang/en_US/shops.png");
        }

        return shop_texture;
    }

    @Override
    public List<IPurchaseable> getContents(EntityPlayer player) {
        List contents = new ArrayList();
        for (IPurchaseable purchaseable : this.contents) {
            if (purchaseable.canList(player.worldObj, player)) {
                contents.add(purchaseable);
            }
        }
        
        return contents;
    }

    /** Whether or not the shop is currently open at this time or season **/
    @Override
    public boolean isOpen(World world, EntityPlayer player) {
        if (world.difficultySetting == EnumDifficulty.PEACEFUL) return true;
        Weekday day = CalendarHelper.getWeekday(world);
        OpeningHours hours = open.get(world.difficultySetting).opening.get(day);
        if (hours == null) return false;
        else {
            long daytime = CalendarHelper.getTime(world); //0-23999 by default      
            int scaledOpening = CalendarHelper.getScaledTime(hours.open);
            int scaledClosing = CalendarHelper.getScaledTime(hours.close);
            boolean isOpen = daytime >= scaledOpening && daytime <= scaledClosing;
            if (!isOpen) return false;
            else return player != null? getContents(player).size() > 0: true;
        }
    }

    @Override
    public IShop addOpening(EnumDifficulty difficulty, Weekday day, int opening, int closing) {
        OpeningHours hours = new OpeningHours(opening, closing);
        OpeningSettings settings = open.get(difficulty) == null ? new OpeningSettings() : open.get(difficulty);
        settings.opening.put(day, hours);
        open.put(difficulty, settings);
        return this;
    }
    
    public int fix(int i) {
        return Math.min(24000, Math.max(0, i));
    }
    
    @Override
    public IShop addOpening(Weekday day, int opening, int closing) {
        OpeningHours hard = new OpeningHours(opening, closing);
        OpeningHours normal = new OpeningHours(fix(opening - 3000), fix(closing + 2000));
        OpeningHours easy = new OpeningHours(fix(opening - 4000), fix(closing + 5000));
        OpeningSettings hSettings = open.get(EnumDifficulty.HARD) == null? new OpeningSettings() : open.get(EnumDifficulty.HARD);
        OpeningSettings nSettings = open.get(EnumDifficulty.NORMAL) == null? new OpeningSettings() : open.get(EnumDifficulty.NORMAL);
        OpeningSettings eSettings = open.get(EnumDifficulty.EASY) == null? new OpeningSettings() : open.get(EnumDifficulty.EASY);
        hSettings.opening.put(day, hard);
        nSettings.opening.put(day, normal);
        eSettings.opening.put(day, easy);
        open.put(EnumDifficulty.HARD, hSettings);
        open.put(EnumDifficulty.NORMAL, nSettings);
        open.put(EnumDifficulty.EASY, eSettings);
        return this;
    }

    @Override
    public IShop addItem(IPurchaseable item) {
        this.contents.add(item);
        this.registers.add(item);
        return this;
    }
    
    @Override
    public IShop addItem(long cost, ItemStack... items) {
        return addItem(new Purchaseable(cost, items));
    }

    /** Return the welcome message for this shop **/
    @Override
    public String getWelcome() {
        if (greetings.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        if (last < (greetings.size() - 1)) {
            last++;
        } else last = 0;

        return greetings.get(last);
    }

    private static class OpeningSettings {
        private HashMap<Weekday, OpeningHours> opening = new HashMap();
    }

    /** The integers in here are as follows
     *  1000 = 1 AM
     *  2500 = 2:30am
     *  18000 = 6PM
     *  etc. */
    private static class OpeningHours {
        private int open;
        private int close;

        public OpeningHours(int open, int close) {
            this.open = open;
            this.close = close;
        }
    }
}

package joshie.harvestmoon.shops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import joshie.harvestmoon.api.core.Weekday;
import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.api.shops.IShop;
import joshie.harvestmoon.api.shops.IShopGuiOverlay;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.client.Minecraft;
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
            String key = HMModInfo.MODPATH + ".shop." + name + ".greeting" + i;
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
        ResourceLocation shop_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/shops.png");
        try {
            MCClientHelper.getMinecraft().renderEngine.getTexture(shop_texture).loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (Exception e) {
            shop_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/en_US/shops.png");
        }

        return shop_texture;
    }

    @Override
    public List<IPurchaseable> getContents() {
        return contents;
    }

    /** Whether or not the shop is currently open at this time or season **/
    @Override
    public boolean isOpen(World world) {
        Weekday day = CalendarHelper.getWeekday(world);
        OpeningHours hours = open.get(world.difficultySetting).opening.get(day);
        if (hours == null) return false;
        else {
            long daytime = CalendarHelper.getTime(world); //0-23999 by default      
            int scaledOpening = CalendarHelper.getScaledTime(hours.open);
            int scaledClosing = CalendarHelper.getScaledTime(hours.close);
            return daytime >= scaledOpening && daytime <= scaledClosing;
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

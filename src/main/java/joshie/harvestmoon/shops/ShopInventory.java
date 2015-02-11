package joshie.harvestmoon.shops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.util.generic.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class ShopInventory {
    private HashSet<IPurchaseable> contents = new HashSet();
    protected ArrayList<String> greetings = new ArrayList();
    private int resourceY;
    private String name;
    protected int last;

    public ShopInventory(String name, int resourceY) {
        this.resourceY = resourceY;
        for (int i = 1; i < 32; i++) {
            String key = "hm.shop." + name + ".greeting" + i;
            String greeting = Text.localize(key);
            if (!greeting.equals(key)) {
                greetings.add(greeting);
            }
        }

        Collections.shuffle(greetings);
    }

    /** Returns the location of the shops name **/
    public ResourceLocation getResource() {
        ResourceLocation shop_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/shops.png");
        try {
            Minecraft.getMinecraft().renderEngine.getTexture(shop_texture).loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (Exception e) {
            shop_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/en_US/shops.png");
        }

        return shop_texture;
    }

    /** Returns the y Coordinate of this shop on the texture **/
    public int getResourceY() {
        return resourceY;
    }

    /** Whether or not the shop is currently open at this time or season **/
    public boolean isOpen(World world) {
        return true;
    }

    public void add(IPurchaseable item) {
        this.contents.add(item);
    }

    /** Return the welcome message for this shop **/
    public String getWelcome() {
        if (greetings.size() == 0) {
            return "JOSHIE IS STOOPID AND FORGOT WELCOME LANG";
        }

        if (last < (greetings.size() - 1)) {
            last++;
        } else last = 0;

        return greetings.get(last);
    }
}

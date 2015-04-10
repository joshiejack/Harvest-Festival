package joshie.harvestmoon.shops;

import joshie.harvestmoon.api.shops.IShopGuiOverlay;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ShopInventoryGui implements IShopGuiOverlay {
    private ResourceLocation shop_overlay;
    private int resourceY;
    private String name;
    protected int last;

    public ShopInventoryGui(String name, int resourceY) {
        this.shop_overlay = new ResourceLocation(HMModInfo.MODPATH + ":textures/gui/shops/" + name + ".png");
        this.resourceY = resourceY;
    }

    public ShopInventoryGui(int resourceY) {
        this.resourceY = resourceY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderOverlay(GuiScreen gui, int x, int y, int xSize, int ySize) {
        if (shop_overlay != null) {
            gui.mc.renderEngine.bindTexture(shop_overlay);
            gui.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        }
        
        gui.mc.renderEngine.bindTexture(getResource());
        gui.drawTexturedModalRect(x + 20, y + 5, 1, resourceY, 254, 32);
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
}

package joshie.harvestmoon.gui;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiNPCShop extends GuiNPC {
    private static final ResourceLocation gui_texture = new ResourceLocation(HMModInfo.MODPATH, "textures/gui/shop.png");
    private static final ResourceLocation number_texture = new ResourceLocation(HMModInfo.MODPATH, "lang/en_US/shops.png");
    private static final ResourceLocation coin_texture = new ResourceLocation(HMModInfo.MODPATH, "textures/gui/gui_elements.png");
    private static ResourceLocation name_texture;
    private static ResourceLocation shop_overlay;
    private ShopInventory shop;
    private boolean welcome;
    private int resourceY;

    public GuiNPCShop(EntityNPC npc, EntityPlayer player) {
        super(npc, player);

        shop = npc.getNPC().getShop();
        if (shop != null && shop.isOpen(player.worldObj)) {
            shop_overlay = shop.getOverlay();
            name_texture = shop.getResource();
            resourceY = shop.getResourceY();
        } else player.closeScreen();
    }

    @Override
    public void drawBackground(int x, int y) {
        if (!welcome) super.drawBackground(x, y);
        else {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            mc.renderEngine.bindTexture(gui_texture);
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
            mc.renderEngine.bindTexture(shop_overlay);
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
            mc.renderEngine.bindTexture(name_texture);
            drawTexturedModalRect(x + 20, y + 5, 1, resourceY, 254, 32);
            drawCoinage(x, y, HarvestMoon.handler.getClient().getPlayerData().getGold());
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    private void drawCoinage(int x, int y, int gold) {        
        mc.renderEngine.bindTexture(number_texture);
        int width = 0;
        int pos = 0;
        char[] digits = String.valueOf(gold).toCharArray();
        for (int i = (digits.length - 1); i >= 0; i--) {
            Number num = null;
            if(pos == 3) {
                pos = -1;
                num = Number.COMMA;
                i++;
            } else num = Number.values()[Character.getNumericValue(digits[i])];
            width += num.getWidth();
            drawTexturedModalRect((x + 230) - width, y + 16, num.getX(), 232, num.getWidth(), 12);
            
            pos++;
        }
        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        mc.renderEngine.bindTexture(coin_texture);
        mc.ingameGUI.drawTexturedModalRect((x + 230) - width - 15, y + 15, 244, 0, 12, 12);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void drawForeground(int x, int y) {
        if (!welcome) super.drawForeground(x, y);
        else {
            
        }
    }

    @Override
    public void endChat() {
        welcome = true;
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        if (!welcome) super.onMouseClick(mouseX, mouseY);
        else {

        }
    }
    
    private static enum Number {
        ZERO(1, 9), ONE(11, 7), TWO(19, 9), THREE(29, 8), FOUR(38, 9), FIVE(48, 8), SIX(57, 9), SEVEN(67, 8), EIGHT(76, 8), NINE(85, 9), COMMA(95, 4);
        
        private final int xStart;
        private final int width;
        
        private Number(int x, int w) {
            this.xStart = x;
            this.width = w;
        }
        
        public int getX() {
            return xStart;
        }
        
        public int getWidth() {
            return width;
        }
    }
}

package joshie.harvestmoon.gui;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiNPCShop extends GuiNPC {
    private static final ResourceLocation gui_texture = new ResourceLocation(HMModInfo.MODPATH, "textures/gui/shop.png");
    private static ResourceLocation name_texture;
    private ShopInventory shop;
    private boolean welcome;
    private int resourceY;

    public GuiNPCShop(EntityNPC npc, EntityPlayer player) {
        super(npc, player);

        shop = npc.getNPC().getShop();
        if (shop != null && shop.isOpen(player.worldObj)) {
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
            mc.renderEngine.bindTexture(name_texture);
            drawTexturedModalRect(x + 20, y + 5, 1, resourceY, 254, 32);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
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
}

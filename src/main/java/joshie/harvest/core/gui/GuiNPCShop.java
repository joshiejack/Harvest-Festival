package joshie.harvest.core.gui;

import java.util.ArrayList;
import java.util.List;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketPurchaseItem;
import joshie.harvest.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiNPCShop extends GuiNPC {
    protected static final ResourceLocation gui_texture = new ResourceLocation(HFModInfo.MODPATH, "textures/gui/shop.png");
    protected static final ResourceLocation number_texture = new ResourceLocation(HFModInfo.MODPATH, "lang/en_US/shops.png");
    protected static final ResourceLocation shelve_texture = new ResourceLocation(HFModInfo.MODPATH, "textures/gui/shop_extra.png");
    protected List<IPurchaseable> contents;
    protected IShopGuiOverlay overlay;
    protected boolean welcome;
    protected int start;

    public GuiNPCShop(EntityNPC npc, EntityPlayer player) {
        super(npc, player);

        IShop shop = npc.getNPC().getShop();
        if (shop == null || !shop.isOpen(player.worldObj, player)) player.closeScreen();
        overlay = shop.getGuiOverlay();
        contents = shop.getContents(player);
    }

    public void setStart(int i) {
        start = Math.max(0, Math.min(contents.size() - getMax(), i));
    }

    @Override
    public void drawBackground(int x, int y) {
        if (!welcome) super.drawBackground(x, y);
        else {
            y += 20; //Add 20

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            mc.renderEngine.bindTexture(gui_texture);
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
            overlay.renderOverlay(this, x, y, xSize, ySize);
            drawCoinage(x, y, PlayerHelper.getGold(player));
            drawShelves(x, y);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    protected void drawShelves(int x, int y) {
        int index = 0;
        for (int i = start; i < contents.size(); i++) {
            if (index > 9) break;
            IPurchaseable purchaseable = contents.get(i);
            ItemStack display = purchaseable.getDisplayStack();
            long cost = purchaseable.getCost();
            mc.renderEngine.bindTexture(shelve_texture);
            int indexPercent = index % 2;
            int indexDivide = index / 2;
            int percent99 = indexPercent * 99;
            int percent37 = indexDivide * 37;
            int x99 = percent99 + x;
            int y37 = y + percent37;

            drawTexturedModalRect(x + 29 + (indexPercent * 101), y37 + 41, 0, 0, 160, 32);
            int xOffset = 0;
            int posY = 41 + percent37;
            int posX = 98 + percent99;

            if (mouseY >= posY + 23 && mouseY <= posY + 46 && mouseX >= posX - 63 && mouseX <= posX - 41) {
                List list = new ArrayList();
                purchaseable.addTooltip(list);
                addTooltip(list);
            }

            if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 32) {
                xOffset = 32;
            }

            drawTexturedModalRect(x + posX, y + posY, xOffset, 32, 32, 32);
            //mc.fontRenderer.drawStringWithShadow(display.getDisplayName(), x + 60, y + 46 + (index * 37), 0xC39753);
            StackHelper.drawStack(display, x99 + 36, y37 + 46, 1.4F);
            mc.renderEngine.bindTexture(HFModInfo.elements);
            drawTexturedModalRect(x99 + 59, y37 + 50, 244, 0, 12, 12);
            
            mc.fontRenderer.drawStringWithShadow("" + cost, x99 + 73, y37 + 53, 0xC39753);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            index++;

            if (index >= 10) {
                break;
            }
        }
    }

    private void drawCoinage(int x, int y, long gold) {
        mc.renderEngine.bindTexture(number_texture);
        int width = 0;
        int pos = 0;
        char[] digits = String.valueOf(gold).toCharArray();
        for (int i = (digits.length - 1); i >= 0; i--) {
            Number num = null;
            if (pos == 3) {
                pos = -1;
                num = Number.COMMA;
                i++;
            } else num = Number.values()[Character.getNumericValue(digits[i])];
            width += num.getWidth();
            drawTexturedModalRect((x + 230) - width, y + 16, num.getX(), 232, num.getWidth(), 12);

            pos++;
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        mc.renderEngine.bindTexture(HFModInfo.elements);
        mc.ingameGUI.drawTexturedModalRect((x + 230) - width - 15, y + 15, 244, 0, 12, 12);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void drawForeground(int x, int y) {
        if (!welcome) super.drawForeground(x, y);
    }

    @Override
    public void endChat() {
        welcome = true;
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (key == 208 || character == 's') {
            setStart(start + 2);
        }

        if (key == 200 || character == 'w') {
            setStart(start - 2);
        }

        super.keyTyped(character, key);
    }

    @Override
    protected void onMouseClick(int x, int y) {
        if (!welcome) super.onMouseClick(x, y);
        else {
            int index = 0;
            for (int i = start; i < contents.size(); i++) {
                if (index > 9) break;
                IPurchaseable purchaseable = contents.get(i);
                if (purchaseable.canBuy(player.worldObj, player)) {
                    long cost = purchaseable.getCost();
                    int indexPercent = index % 2;
                    int indexDivide = index / 2;
                    int percent99 = indexPercent * 99;
                    int percent37 = indexDivide * 37;
                    int posY = 41 + percent37;
                    int posX = 98 + percent99;

                    if (PlayerHelper.getGold(player) - purchaseable.getCost() >= 0) {
                        if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 32) {
                            PacketHandler.sendToServer(new PacketPurchaseItem(purchaseable));
                        }
                    }

                    index++;
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        if (mouseWheel != 0) {
            if (mouseWheel < 0) setStart(start + getIncrease());
            else setStart(start - getIncrease());
        }
    }
    
    public int getIncrease() {
        return 2;
    }
    
    public int getMax() {
        return 10;
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

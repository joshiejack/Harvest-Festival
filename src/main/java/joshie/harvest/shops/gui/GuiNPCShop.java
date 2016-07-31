package joshie.harvest.shops.gui;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.shops.packets.PacketPurchaseItem;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.npc.gui.GuiNPCBase;
import joshie.harvest.player.stats.StatDataClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiNPCShop extends GuiNPCBase {
    protected static final ResourceLocation gui_texture = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop.png");
    protected static final ResourceLocation number_texture = new ResourceLocation(HFModInfo.MODID, "lang/en_US/shops.png");
    protected static final ResourceLocation shelve_texture = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop_extra.png");
    protected StatDataClient stats;
    protected List<IPurchaseable> contents;
    protected IShopGuiOverlay overlay;
    protected int start;

    public GuiNPCShop(AbstractEntityNPC npc, EntityPlayer player) {
        super(npc, player, -1);

        IShop shop = npc.getNPC().getShop();
        if (shop == null || !shop.isOpen(player.worldObj, player)) player.closeScreen();
        overlay = shop.getGuiOverlay();
        contents = shop.getContents(player);
        stats = HFTrackers.getClientPlayerTracker().getStats();
    }

    public void setStart(int i) {
        start = Math.max(0, Math.min(contents.size() - getMax(), i));
    }

    @Override
    public void drawBackground(int x, int y) {
        y += 20; //Add 20

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        mc.renderEngine.bindTexture(gui_texture);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        overlay.renderOverlay(this, x, y, xSize, ySize);
        drawCoinage(x, y, stats.getGold());
        drawShelves(x, y);
        mc.renderEngine.bindTexture(shelve_texture);

        int up = 0;
        int down = 0;
        if (mouseX >= 231 && mouseX <= 242) {
            up = mouseY >= 66 && mouseY <= 75 ? 17 : 0;
            down = mouseY >= 231 && mouseY <= 240 ? 17 : 0;
        }

        drawTexturedModalRect(x + 230, y + 45, 72 + up, 34, 14, 11);
        drawTexturedModalRect(x + 230, y + 210, 72 + down, 47, 14, 11);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
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
                List<String> list = new ArrayList<String>();
                purchaseable.addTooltip(list);
                addTooltip(list);
            }

            if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 28) {
                xOffset = 32;
            }

            drawTexturedModalRect(x + posX, y + posY, xOffset, 32, 32, 32);
            GlStateManager.enableBlend();
            mc.renderEngine.bindTexture(HFModInfo.elements);
            drawTexturedModalRect(x99 + 59, y37 + 50, 244, 0, 12, 12);

            mc.fontRendererObj.drawStringWithShadow("" + cost, x99 + 73, y37 + 53, 0xC39753);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            StackHelper.drawStack(display, x99 + 36, y37 + 46, 1.4F);

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

        GlStateManager.disableDepth();
        mc.renderEngine.bindTexture(HFModInfo.elements);
        mc.ingameGUI.drawTexturedModalRect((x + 230) - width - 15, y + 15, 244, 0, 12, 12);
        GlStateManager.enableDepth();
    }

    @Override
    public void drawForeground(int x, int y) {
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
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
        int index = 0;
        for (int i = start; i < contents.size(); i++) {
            if (index > 9) break;
            IPurchaseable purchaseable = contents.get(i);
            if (purchaseable.canBuy(player.worldObj, player)) {
                int indexPercent = index % 2;
                int indexDivide = index / 2;
                int percent99 = indexPercent * 99;
                int percent37 = indexDivide * 37;
                int posY = 41 + percent37;
                int posX = 98 + percent99;

                if (stats.getGold() - purchaseable.getCost() >= 0) {
                    if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 32) {
                        PacketHandler.sendToServer(new PacketPurchaseItem(purchaseable));
                    }
                }

                index++;
            }
        }

        boolean up = false;
        boolean down = false;
        if (mouseX >= 231 && mouseX <= 242) {
            up = mouseY >= 66 && mouseY <= 75;
            down = mouseY >= 231 && mouseY <= 240;
        }

        if (down) setStart(start + getIncrease());
        else if (up) setStart(start - getIncrease());
    }

    @Override
    public void handleMouseInput() throws IOException {
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

    private enum Number {
        ZERO(1, 9), ONE(11, 7), TWO(19, 9), THREE(29, 8), FOUR(38, 9), FIVE(48, 8), SIX(57, 9), SEVEN(67, 8), EIGHT(76, 8), NINE(85, 9), COMMA(95, 4);

        private final int xStart;
        private final int width;

        Number(int x, int w) {
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
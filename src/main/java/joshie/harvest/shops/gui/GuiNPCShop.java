package joshie.harvest.shops.gui;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.ShopFontRenderer;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.GuiNPCBase;
import joshie.harvest.player.stats.StatsClient;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.packet.PacketPurchaseItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiNPCShop extends GuiNPCBase {
    protected static final ResourceLocation gui_texture = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop.png");
    protected static final ResourceLocation shelve_texture = new ResourceLocation(HFModInfo.MODID, "textures/gui/shop_extra.png");
    protected final StatsClient stats;
    protected final List<IPurchasable> contents;
    protected final IShopGuiOverlay overlay;
    protected final Shop shop;
    protected int start;

    public GuiNPCShop(EntityPlayer player, EntityNPC npc, int nextGui) {
        super(player, npc, EnumHand.MAIN_HAND, nextGui);

        shop = npc.getNPC().getShop();
        if (shop == null || !shop.isOpen(player.worldObj, player)) player.closeScreen();
        overlay = shop.getGuiOverlay();
        contents = shop.getContents(player);
        stats = HFTrackers.getClientPlayerTracker().getStats();
    }

    public void setStart(int i) {
        start = Math.max(0, Math.min(getMaximumSize(), i));
    }

    @Override
    public void drawBackground(int x, int y) {
        y += 20; //Add 20

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        mc.renderEngine.bindTexture(gui_texture);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (overlay != null) overlay.renderOverlay(this, x, y, xSize, ySize);
        ShopFontRenderer.render(this, x + 20, y + 16, shop.getLocalizedName(), false);
        drawCoinage(x, y, stats.getGold());
        drawShelves(x, y);
        mc.renderEngine.bindTexture(shelve_texture);

        int up = 0;
        int down = 0;
        if (mouseX >= 231 && mouseX <= 242) {
            up = mouseY >= 66 && mouseY <= 75 ? 17 : 0;
            down = mouseY >= 231 && mouseY <= 240 ? 17 : 0;
        }

        if (start != 0) drawTexturedModalRect(x + 230, y + 45, 72 + up, 34, 14, 11);
        if (start < getMaximumSize()) drawTexturedModalRect(x + 230, y + 210, 72 + down, 47, 14, 11);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    protected String getCostAsString(long cost) {
        if (cost < 1000) return "" + cost;
        long remainder = cost % 1000;
        int decimal = remainder == 0 ? 0 : remainder % 100 == 0 ? 1: remainder %10 == 0 ? 2: 3;
        int exp = (int) (Math.log(cost) / Math.log(1000));
        return String.format("%." + decimal + "f%c", cost / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
    }

    protected void drawShelves(int x, int y) {
        int index = 0;
        for (int i = start; i < contents.size(); i++) {
            if (index > (getMax())) break;
            IPurchasable purchaseable = contents.get(i);
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
                List<String> list = new ArrayList<>();
                purchaseable.addTooltip(list);
                addTooltip(list);
            }

            if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 28) {
                xOffset = 32;
            }

            drawTexturedModalRect(x + posX, y + posY, xOffset, 32, 32, 32);
            GlStateManager.enableBlend();
            mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
            drawTexturedModalRect(x99 + 59, y37 + 50, 244, 0, 12, 12);

            mc.fontRendererObj.drawStringWithShadow(getCostAsString(cost), x99 + 73, y37 + 53, 0xFFFFFF);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            StackHelper.drawStack(display, x99 + 36, y37 + 46, 1.4F);

            index++;
        }
    }

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    private void drawCoinage(int x, int y, long gold) {
        String formatted = String.valueOf(formatter.format(gold));
        ShopFontRenderer.render(this, x + 210, y + 16, formatted, true);
        GlStateManager.disableDepth();
        mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
        mc.ingameGUI.drawTexturedModalRect((x + 230) - 15, y + 15, 244, 0, 12, 12);
        GlStateManager.enableDepth();
    }

    @Override
    public void drawForeground(int x, int y) {}

    @Override
    public void drawOverlay(int x, int y) {}

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
            if (index > (getMax())) break;
            IPurchasable purchaseable = contents.get(i);
            if (purchaseable.canBuy(player.worldObj, player)) {
                int indexPercent = index % 2;
                int indexDivide = index / 2;
                int percent99 = indexPercent * 99;
                int percent37 = indexDivide * 37;
                int posY = 41 + percent37;
                int posX = 98 + percent99;

                if (stats.getGold() - purchaseable.getCost() >= 0) {
                    if (mouseY >= posY + 20 && mouseY <= posY + 52 && mouseX >= posX && mouseX <= posX + 32) {
                        for (int j = 0; j < (GuiScreen.isShiftKeyDown() ? 64: 1); j++) {
                            PacketHandler.sendToServer(new PacketPurchaseItem(shop.resourceLocation, purchaseable));
                        }
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

        if (down && start < getMaximumSize()) setStart(start + getIncrease());
        else if (up && start != 0) setStart(start - getIncrease());
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
        return 9;
    }

    private int getMaximumSize() {
        int max = contents.size() - getMax();
        max = max %2 == 0 ? max: max - 1;
        return max;
    }
}
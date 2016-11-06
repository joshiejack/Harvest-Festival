package joshie.harvest.shops.gui.button;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.stats.StatsClient;
import joshie.harvest.shops.gui.GuiNPCShop;
import joshie.harvest.shops.packet.PacketPurchaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.shops.gui.GuiNPCShop.SHOP_EXTRA;

public class ButtonListing<I extends IPurchasable> extends GuiButton {
    public static final int HOVER = 2;
    protected final GuiNPCShop shop;
    protected final I purchasable;
    protected int state;

    @SuppressWarnings("unchecked")
    public ButtonListing(GuiNPCShop shop, IPurchasable purchasable, int buttonId, int x, int y) {
        super(buttonId, x, y, purchasable.getDisplayName());
        this.height = 18;
        this.shop = shop;
        this.purchasable = (I) purchasable;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(SHOP_EXTRA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int originalState = getHoverState(hovered);
            state = originalState;
            if (state == HOVER && !canPurchase1()) {
                state = 1;
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawBackground();
            mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!enabled) {
                j = 10526880;
            } else if (state == HOVER && hovered) {
                j = 16777120;
            }

            drawForeground(mc, fontrenderer, j);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            if (originalState == HOVER) {
                List<String> list = new ArrayList<>();
                purchasable.addTooltip(list);
                shop.addTooltip(list);
            }
        }
    }

    protected void drawBackground() {
        drawTexturedModalRect(xPosition, yPosition, 0, state * 18, width / 2, height);
        drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, state * 18, width / 2, height);
    }

    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        StackHelper.drawStack(purchasable.getDisplayStack(), xPosition + 2, yPosition + 1, 1F);
        drawString(fontrenderer, displayString, xPosition + 20, yPosition + (height - 8) / 2, j);
        //Draw the cost
        String cost = shop.getCostAsString(purchasable.getCost());
        int width = fontrenderer.getStringWidth(cost);
        mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
        drawTexturedModalRect(xPosition + 184, (yPosition + (height - 8) / 2) - 2, 244, 0, 12, 12);
        drawString(fontrenderer, cost, xPosition + 180 - width, yPosition + (height - 8) / 2, j);
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        if (state == HOVER) super.playPressSound(soundHandlerIn);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        boolean purchased10 = GuiScreen.isShiftKeyDown() && canPurchase10();
        if (purchased10 || canPurchase1()) {
            PacketHandler.sendToServer(new PacketPurchaseItem(shop.getShop(), purchasable, purchased10 ? 10: 1));
        }
    }

    protected boolean canPurchase10() {
        StatsClient stats = HFTrackers.getClientPlayerTracker().getStats();
        return stats.getGold() - (purchasable.getCost() * 10) >= 0 && purchasable.canBuy(MCClientHelper.getWorld(), MCClientHelper.getPlayer(), 10);
    }

    protected boolean canPurchase1() {
        StatsClient stats = HFTrackers.getClientPlayerTracker().getStats();
        return stats.getGold() - purchasable.getCost() >= 0 && purchasable.canBuy(MCClientHelper.getWorld(), MCClientHelper.getPlayer(), 1);
    }
}

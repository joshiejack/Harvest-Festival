package joshie.harvest.shops.gui.button;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.stats.StatsClient;
import joshie.harvest.shops.data.ShopData;
import joshie.harvest.shops.gui.GuiNPCShop;
import joshie.harvest.shops.packet.PacketPurchaseItem;
import joshie.harvest.town.TownHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.shops.gui.GuiNPCShop.SHOP_EXTRA;

public class ButtonListing<I extends IPurchasable> extends GuiButton {
    private static final int HOVER = 2;
    protected final GuiNPCShop shop;
    protected final I purchasable;
    protected int state;
    private int hoverTimer;
    private int stackSize = 1;
    private boolean clicked;
    private final ShopData data;

    @SuppressWarnings("unchecked")
    public ButtonListing(GuiNPCShop shop, IPurchasable purchasable, int buttonId, int x, int y) {
        super(buttonId, x, y, purchasable.getDisplayName());
        this.height = 18;
        this.shop = shop;
        this.purchasable = (I) purchasable;
        this.data = TownHelper.getClosestTownToEntity(MCClientHelper.getPlayer(), false).getShops();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(SHOP_EXTRA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
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

            if (originalState == HOVER && purchasable.getCost() >= 0) {
                List<String> list = new ArrayList<>();
                purchasable.addTooltip(list);
                shop.addTooltip(list);
            }
        }
    }

    private void drawBackground() {
        drawTexturedModalRect(x, y, 0, state * 18, width / 2, height);
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, state * 18, width / 2, height);
    }

    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        StackRenderHelper.drawStack(purchasable.getDisplayStack(), x + 2, y + 1, 1F);
        drawString(fontrenderer, displayString, x + 20, y + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        //Draw the cost
        String cost = shop.getCostAsString(data.getSellValue(shop.getShop(), purchasable));
        if (purchasable.getCost() != 0) {
            int width = fontrenderer.getStringWidth(cost);
            drawString(fontrenderer, cost, x + 180 - width, y + (height - 8) / 2, j);
            mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
            drawTexturedModalRect(x + 184, (y + (height - 8) / 2) - 2, 244, 0, 12, 12);
        } else {
            int width = fontrenderer.getStringWidth(cost);
            drawString(fontrenderer, cost, x + 194 - width, y + (height - 8) / 2, j);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        clicked = super.mousePressed(mc, mouseX, mouseY);
        return clicked;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (clicked && hovered) {
            if (hoverTimer == 0 || (hoverTimer %20 == 0 && GuiScreen.isCtrlKeyDown())) {
                if (GuiScreen.isShiftKeyDown() && canPurchaseX(stackSize + 10)) {
                    stackSize += 10;
                    StatsClient stats = HFTrackers.getClientPlayerTracker().getStats();
                    stats.setGoldValue(stats.getGold() - (data.getSellValue(shop.getShop(), purchasable) * 10));
                    if (purchasable.getCost() >= 0) shop.updatePurchased(StackHelper.toStack(purchasable.getDisplayStack(), 10 * purchasable.getDisplayStack().getCount()), 10 * purchasable.getDisplayStack().getCount());
                    if (hoverTimer %40 == 1) playPressSound(mc.getSoundHandler());
                } else if (canPurchaseX(stackSize + 1)) {
                    stackSize++;
                    if (purchasable.getCost() >= 0) shop.updatePurchased(purchasable.getDisplayStack(), purchasable.getDisplayStack().getCount());
                    StatsClient stats = HFTrackers.getClientPlayerTracker().getStats();
                    stats.setGoldValue(stats.getGold() - data.getSellValue(shop.getShop(), purchasable));
                    if (hoverTimer %40 == 1) playPressSound(mc.getSoundHandler());
                }

                hoverTimer++;
            }

            if (GuiScreen.isCtrlKeyDown()) {
                hoverTimer++;
            }
        } else {
            hoverTimer = 0;
            stackSize = 0;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (stackSize > 0) {
            PacketHandler.sendToServer(new PacketPurchaseItem(shop.getShop(), purchasable, stackSize));
            stackSize = 0; //Reset
        }

        //Reset the clicking timer
        clicked = false;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        if (state == HOVER) super.playPressSound(soundHandlerIn);
    }

    private boolean canPurchaseX(int x) {
        StatsClient stats = HFTrackers.getClientPlayerTracker().getStats();
        return (purchasable.getStock() == 0 || x <= purchasable.getStock()) && stats.getGold() - (purchasable.getCost() * x) >= 0 && purchasable.canDo(MCClientHelper.getWorld(), MCClientHelper.getPlayer(), x);
    }

    private boolean canPurchase1() {
        return canPurchaseX(1);
    }
}

package uk.joshiejack.economy.client.gui.button;

import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.economy.network.PacketPurchaseItem;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.Sublisting;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ButtonListing extends GuiButton {
    private static final int HOVER = 2;
    protected final GuiShop shop;
    protected final Listing listing;
    protected final Sublisting sublisting;
    private final Wallet wallet; //The active wallet
    protected int state;
    private int hoverTimer;
    private int stackSize = 1;
    private boolean clicked;
    //private final ShopData data;

    @SuppressWarnings("unchecked")
    public ButtonListing(GuiShop shop, Listing listing, int buttonId, int x, int y) {
        super(buttonId, x, y, listing.getSubListing(shop.stock).getDisplayName());
        this.height = 18;
        this.shop = shop;
        this.listing = listing;
        this.sublisting = listing.getSubListing(shop.stock);
        this.wallet = Wallet.getActive();
        //this.data = TownHelper.getClosestTownToEntity(MCClientHelper.getPlayer(), false).getShops();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiShop.EXTRA);
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

            if (originalState == HOVER && listing.getGoldCost(shop.target.player, shop.stock) >= 0) {
                List<String> list = new ArrayList<>();
                sublisting.addTooltip(list);
                shop.addTooltip(list);
            }
        }
    }

    private void drawBackground() {
        drawTexturedModalRect(x, y, 0, state * 18, width / 2, height);
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, state * 18, width / 2, height);
    }

    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
        GlStateManager.enableDepth();
        StackRenderHelper.drawStack(listing.getSubListing(shop.stock).getDisplayStack(), x + 2, y + 1, 1F);
        drawString(fontrenderer, displayString, x + 20, y + (height - 8) / 2, j);
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        //Draw the cost
        long goldCost = listing.getGoldCost(shop.target.player, shop.stock);
        String cost = shop.getCostAsString(goldCost);
        if (goldCost != 0) {
            int width = fontrenderer.getStringWidth(cost);
            drawString(fontrenderer, cost, x + 180 - width, y + (height - 8) / 2, j);
            mc.renderEngine.bindTexture(GuiShop.EXTRA);
            drawTexturedModalRect(x + 184, (y + (height - 8) / 2) - 2, 244, 244, 12, 12);
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
                ItemStack display = listing.getSubListing(shop.stock).getDisplayStack();
                long goldCost = listing.getGoldCost(shop.target.player, shop.stock);
                if (GuiScreen.isShiftKeyDown() && canPurchaseX(stackSize + 10)) {
                    stackSize += 10;
                    wallet.setBalance(wallet.getBalance() - goldCost * 10);
                    if (goldCost >= 0) shop.updatePurchased(StackHelper.toStack(display, 10 * display.getCount()), 10 * display.getCount());
                    if (hoverTimer %40 == 1) playPressSound(mc.getSoundHandler());
                } else if (canPurchaseX(stackSize + 1)) {
                    stackSize++;
                    if (goldCost >= 0) shop.updatePurchased(display, display.getCount());
                    wallet.setBalance(wallet.getBalance() - goldCost);
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
            PenguinNetwork.sendToServer(new PacketPurchaseItem(shop.shop, listing, stackSize));
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
        return wallet.getBalance() - (listing.getGoldCost(shop.target.player, shop.stock) * x) >= 0 && listing.canPurchase(shop.target.player, shop.stock, x);
    }

    private boolean canPurchase1() {
        return canPurchaseX(1);
    }
}

package uk.joshiejack.economy.client.gui.label;

import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class LabelShippingLog extends LabelBook {
    private long total;
    private long overall;

    public LabelShippingLog(GuiBook gui, int x, int y) {
        super(gui, x, y);
        this.total = 0L;
        for (Shipping.HolderSold holderSold : Shipped.getShippingLog()) {
            this.total += holderSold.getValue();
        }

        this.overall = Wallet.getWallet(Wallet.Type.PERSONAL).getIncome() + Wallet.getWallet(Wallet.Type.SHARED).getIncome();
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        GlStateManager.disableDepth();
        //Draw the shipping log
        mc.fontRenderer.drawStringWithShadow(TextFormatting.UNDERLINE + "Shipping Log", x + 173, y - 10, 0xFFFFFF);
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);

        drawHorizontalLine(x + 143 - 6, x + 143 + 115, y, 0xFFB0A483);
        drawHorizontalLine(x + 143 - 5, x + 143 + 116, y + 1, 0xFF9C8C63);

        //Draw the statistics
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Combined Earnings", x + 163, y + 128, 0x857754); //TODO: Allow editing of town name if player is town owner
        drawHorizontalLine(x + 143 - 6, x + 143 + 115, y + 138, 0xFFB0A483);
        drawHorizontalLine(x + 143 - 5, x + 143 + 116, y + 139, 0xFF9C8C63);
        mc.fontRenderer.setUnicodeFlag(flag);

        //Draw the shipping log totals
        if (total > 0L) {
            GlStateManager.color(1F, 1F, 1F, 1F);
            mc.getTextureManager().bindTexture(GuiShop.EXTRA);
            drawTexturedModalRect(x + 140 + 18, y + 110 + 4, 244, 244, 12, 12);
            String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(total);
            mc.fontRenderer.drawStringWithShadow(text, x + 140 + 32, y + 110 + 6, 0xFFFFFFFF);
        }

        //Draw all time earnings
        if (total > 0L) {
            GlStateManager.color(1F, 1F, 1F, 1F);
            mc.getTextureManager().bindTexture(GuiShop.EXTRA);
            drawTexturedModalRect(x + 140 + 18, y + 138 + 4, 244, 244, 12, 12);
            String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(overall);
            mc.fontRenderer.drawStringWithShadow(text, x + 140 + 32, y + 138 + 6, 0xFFFFFFFF);
        }

        GlStateManager.enableDepth();
    }
}


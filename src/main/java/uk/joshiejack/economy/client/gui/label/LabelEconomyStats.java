package uk.joshiejack.economy.client.gui.label;

import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.gold.Wallet;
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
public class LabelEconomyStats extends LabelBook {
    private boolean isSharedWalletActive;
    private Wallet personal, shared;
    public LabelEconomyStats(GuiBook gui, int x, int y) {
        super(gui, x, y);
        personal = Wallet.getWallet(Wallet.Type.PERSONAL);
        shared = Wallet.getWallet(Wallet.Type.SHARED);
        isSharedWalletActive = Wallet.getActive() == shared;
    }

    private static String formatGold(long value) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(value);
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        GlStateManager.disableDepth();
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        //Draw your personal balance
        mc.fontRenderer.setUnicodeFlag(true);
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Personal account", x, y, gui.fontColor1);
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Shared account", x, y + 100, gui.fontColor1);
        drawHorizontalLine(x - 6, x + 115, y + 46, 0xFFB0A483);
        drawHorizontalLine(x - 5, x + 116, y + 46 + 1, 0xFF9C8C63);
        drawHorizontalLine(x - 6, x + 115, y + 96, 0xFFB0A483);
        drawHorizontalLine(x - 5, x + 116, y + 96 + 1, 0xFF9C8C63);
        mc.fontRenderer.setUnicodeFlag(false);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "^", x + 55, y + 64, gui.fontColor1);
        int length = mc.fontRenderer.getStringWidth("Transfer");
        mc.fontRenderer.drawStringWithShadow("Transfer", x + 60 - length / 2, y + 67, 0xFFFFFF);
        mc.fontRenderer.drawString(TextFormatting.BOLD + "v", x + 55, y + 74, gui.fontColor1);

        GlStateManager.pushMatrix();
        float scale = 0.6666666666F;
        GlStateManager.scale(scale, scale, scale);
        ////Personal Account
        //Expenses
        mc.fontRenderer.drawStringWithShadow("Expenses", (x) / scale, (y + 10) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(personal.getExpenses()), (x + 45) / scale, (y + 10) / scale, 0xFFFFFFFF);
        //Income
        mc.fontRenderer.drawStringWithShadow("Income", (x) / scale, (y + 19) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(personal.getIncome()), (x + 45) / scale, (y + 19) / scale, 0xFFFFFFFF);
        //Profit
        mc.fontRenderer.drawStringWithShadow("Profit", (x) / scale, (y + 28) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(personal.getIncome() - personal.getExpenses()), (x + 45) / scale, (y + 28) / scale, 0xFFFFFFFF);
        //Balance
        mc.fontRenderer.drawStringWithShadow("Balance", (x) / scale, (y + 37) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(personal.getBalance()), (x + 45) / scale, (y + 37) / scale, 0xFFFFFFFF);

        ////Shared Account
        //Expenses
        mc.fontRenderer.drawStringWithShadow("Expenses", (x) / scale, (y + 110) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(shared.getExpenses()), (x + 45) / scale, (y + 110) / scale, 0xFFFFFFFF);
        //Income
        mc.fontRenderer.drawStringWithShadow("Income", (x) / scale, (y + 119) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(shared.getIncome()), (x + 45) / scale, (y + 119) / scale, 0xFFFFFFFF);
        //Profit
        mc.fontRenderer.drawStringWithShadow("Profit", (x) / scale, (y + 128) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(shared.getIncome() - shared.getExpenses()), (x + 45) / scale, (y + 128) / scale, 0xFFFFFFFF);
        //Balance
        mc.fontRenderer.drawStringWithShadow("Balance", (x) / scale, (y + 137) / scale, 0xFFFFFFFF);
        mc.fontRenderer.drawStringWithShadow(formatGold(shared.getBalance()), (x + 45) / scale, (y + 137) / scale, 0xFFFFFFFF);

        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(GuiShop.EXTRA);
        drawTexturedModalRect((x + 35) / scale, (y + 9)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 18)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 27)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 36)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 109)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 118)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 127)/scale, 244, 244, 12, 12);
        drawTexturedModalRect((x + 35) / scale, (y + 136)/scale, 244, 244, 12, 12);
        GlStateManager.popMatrix();
        //Draw the shared balance

        //mc.fontRenderer.drawString("Shared account", x, y + 20, gui.fontColor1);
        GlStateManager.color(1F, 1F, 1F, 1F);
        //mc.getTextureManager().bindTexture(GOLD);
        //drawTexturedModalRect(x, y + 30, 244, 244, 12, 12);
        //mc.fontRenderer.drawStringWithShadow(text2, x + 14, y + 32, 0xFFFFFFFF);

        mc.fontRenderer.setUnicodeFlag(flag);

        GlStateManager.enableDepth();
    }
}


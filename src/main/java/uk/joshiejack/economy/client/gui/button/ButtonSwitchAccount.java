package uk.joshiejack.economy.client.gui.button;

import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.economy.network.PacketSwitchWalletUsed;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

public class ButtonSwitchAccount extends ButtonBook {
    private final Wallet.Type type;
    private final Wallet wallet;
    private int personalX, personalY;
    private int sharedX, sharedY;

    public ButtonSwitchAccount(Wallet.Type type, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.width = 7;
        this.height = 8;
        this.type = type;
        this.wallet = Wallet.getWallet(type);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            boolean isPersonalWallet = Wallet.getActive() == Wallet.getWallet(Wallet.Type.PERSONAL);
            personalX = (isPersonalWallet ? 16 : 17) + mc.fontRenderer.getStringWidth("Personal account");
            personalY = 1;
            sharedX = (!isPersonalWallet ? 14 : 15) + mc.fontRenderer.getStringWidth("Shared account");
            sharedY = 101;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(gui.left); //Elements
            int extraX = type == Wallet.Type.SHARED ? sharedX: personalX;
            int extraY = type == Wallet.Type.SHARED ? sharedY : personalY;
            hovered = mouseX >= x + extraX && mouseY >= y + extraY && mouseX < x + extraX + width && mouseY < y + height + extraY;
            boolean hoveredPersonal = mouseX >= x + personalX && mouseY >= y + personalY && mouseX < x + personalX + width + (isPersonalWallet ? 2 :0) && mouseY < y + height + personalY;
            boolean hoveredShared = mouseX >= x + sharedX && mouseY >= y + sharedY && mouseX < x + sharedX + width + (!isPersonalWallet ? 2 :0) && mouseY < y + height + sharedY;
            if (hoveredPersonal) {
                if (isPersonalWallet) gui.addTooltip(String.format("This wallet is currently %sactive", TextFormatting.GREEN));
                else {
                    gui.addTooltip(String.format("This wallet is currently %sinactive", TextFormatting.RED));
                    gui.addTooltip("Click to switch to this wallet.");
                }
            }

            if (hoveredShared) {
                if (!isPersonalWallet) gui.addTooltip(String.format("This wallet is currently %sactive", TextFormatting.GREEN));
                else {
                    gui.addTooltip(String.format("This wallet is currently %sinactive", TextFormatting.RED));
                    gui.addTooltip("Click to switch to this wallet.");
                }
            }

            //Draw check mark
            drawTexturedModalRect(x + personalX, y + personalY, 31 + (!isPersonalWallet ? 10 : 0) + (hoveredPersonal ? 17 : 0), 248, 7 + (isPersonalWallet ? 3 :0), 8);
            drawTexturedModalRect(x + sharedX, y + sharedY, 31 + (isPersonalWallet ? 10 : 0) + (hoveredShared ? 17 : 0), 248, 7 + (!isPersonalWallet ? 3 :0), 8);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            mc.fontRenderer.setUnicodeFlag(flag);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean isPersonalWallet = Wallet.getActive() == Wallet.getWallet(Wallet.Type.PERSONAL);
        boolean hoveredPersonal = mouseX >= x + personalX && mouseY >= y + personalY && mouseX < x + personalX + width + (isPersonalWallet ? 2 :0) && mouseY < y + height + personalY;
        boolean hoveredShared = mouseX >= x + sharedX && mouseY >= y + sharedY && mouseX < x + sharedX + width + (!isPersonalWallet ? 2 :0) && mouseY < y + height + sharedY;
        return enabled && visible && ((isPersonalWallet && hoveredShared) || (!isPersonalWallet && hoveredPersonal));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PenguinNetwork.sendToServer(new PacketSwitchWalletUsed(Wallet.getActive() == Wallet.getWallet(Wallet.Type.PERSONAL)));
    }
}

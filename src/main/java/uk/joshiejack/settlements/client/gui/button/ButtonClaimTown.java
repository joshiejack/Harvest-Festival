package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.network.town.people.PacketClaimTown;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

public class ButtonClaimTown extends ButtonBook {
    private final int dimension;
    private final int town;

    public ButtonClaimTown(int dimension, int town, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.dimension = dimension;
        this.town = town;
        this.width = 16;
        this.height = 16;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiJournal.ICONS); //Elements
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            drawTexturedModalRect(x, y, hovered && enabled ? 16 : 0, 32, 16, 16);
            if (hovered) {
                gui.addTooltip(StringHelper.format("settlements.message.claim.unclaimed", TextFormatting.RED));
                gui.addTooltip(StringHelper.format("settlements.message.claim.claim", TextFormatting.GREEN));
                gui.addTooltip("");
                gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.RED + StringHelper.localize("settlements.message.claim.warning"), 200));
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        enabled = false; //Town is claimed, let's switch shit up
        visible = false; //Town is claimed, let's switch shit up
        PenguinNetwork.sendToServer(new PacketClaimTown(dimension, town));
    }
}

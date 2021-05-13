package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.network.town.people.PacketToggleOrdinance;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Ordinance;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ButtonOrdinance extends ButtonBook {
    private final int dimension, id;
    private final Ordinance ordinance;
    private boolean enacted;

    public ButtonOrdinance(GuiBook gui, Town<?> town, Ordinance ordinance, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.enabled = PenguinTeamsClient.getInstance().getID().equals(town.getCharter().getTeamID()); //If we are this team
        this.ordinance = ordinance;
        this.width = 16;
        this.height = 16;
        this.enacted = town.getGovernment().hasLaw(ordinance);
        this.dimension = gui.mc.player.world.provider.getDimension();
        this.id = town.getID();
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            //
            mc.fontRenderer.drawString(ordinance.getName(), x - 110, y, gui.fontColor1); //TODO: Allow editing of town name if player is town owner
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawSplitString(mc.fontRenderer.listFormattedStringToWidth(ordinance.getDescription(), 120).get(0), x - 110, y + 8,  120, gui.fontColor2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiJournal.ICONS); //Elements
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            drawTexturedModalRect(x, y, hovered && enabled ? 16 : 0, 16, 16, 16);
            if (hovered) {
                gui.addTooltip(TextFormatting.AQUA + ordinance.getName());
                if (enacted) {
                    gui.addTooltip(StringHelper.format("settlements.ordinance.enabled", TextFormatting.GREEN));
                } else gui.addTooltip(StringHelper.format("settlements.ordinance.disabled", TextFormatting.RED));
                gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.GRAY + " " + TextFormatting.ITALIC + ordinance.getTooltip(), 128));
            }

            //Draw the x/tick
            mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            if (enacted) drawTexturedModalRect(x + 5, y + 8, 31, 248, 10, 8);
            else drawTexturedModalRect(x + 6, y + 8, 41, 248, 7, 8);

            GlStateManager.color(1.0F, 1.0F, 1.0F);
            mc.fontRenderer.setUnicodeFlag(flag);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        enacted = !enacted; //Do this anyways
        PenguinNetwork.sendToServer(new PacketToggleOrdinance(ordinance, dimension, id));
    }
}

package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiJournal;
import uk.joshiejack.settlements.network.town.people.PacketJoinTown;
import uk.joshiejack.settlements.network.town.people.PacketRequestCitizenship;
import uk.joshiejack.settlements.world.town.people.Citizenship;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;

public class ButtonRequestCitizenship extends ButtonBook {
    private final Citizenship type;
    private final int dimension;
    private final int town;

    public ButtonRequestCitizenship(Citizenship type, int dimension, int town, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.dimension = dimension;
        this.type = type;
        this.town = town;
        this.width = 16;
        this.height = 16;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiJournal.ICONS); //Elements
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            drawTexturedModalRect(x, y, hovered && enabled ? 16 : 0, 48, 16, 16);
            if (hovered) {
                String type = this.type == Citizenship.APPLICATION ? "apply" : "join";
                gui.addTooltip(StringHelper.localize("settlements.message.apply.mayor"));
                gui.addTooltip(StringHelper.localize("settlements.message." + type + ".citizenship"));
                gui.addTooltip(Strings.EMPTY);
                gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.RED + StringHelper.localize("settlements.message.apply.warning"), 200));
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        enabled = false; //Town is claimed, let's switch shit up
        visible = false; //Town is claimed, let's switch shit up
        if (type == Citizenship.APPLICATION) {
            PenguinNetwork.sendToServer(new PacketRequestCitizenship(dimension, town));
        } else PenguinNetwork.sendToServer(new PacketJoinTown(dimension, town));
    }
}

package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiNPC;
import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.network.town.people.PacketInviteNPC;
import uk.joshiejack.settlements.npcs.status.Statuses;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

import static uk.joshiejack.settlements.Settlements.MODID;

public class ButtonInviteNPC extends ButtonBook {
    private final NPCDisplayData npc;

    public ButtonInviteNPC(GuiBook gui, NPCDisplayData npc, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "INVITE: " + npc.getLocalizedName()); //TODO: Fancier render
        this.npc = npc;
        this.width = 15;
        this.height = 8;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiNPC.ELEMENTS); //Elements
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            drawTexturedModalRect(x, y, 110, hovered ? 244 : 226, 15, 8);
            if (hovered) {
                gui.addTooltip(StringHelper.format(MODID + ".journal.tooltips.invite"
                        , Statuses.getValue(npc.getRegistryName(), "has_met") == 1 ? npc.getLocalizedName() : "???????"));
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        enabled = false;
        visible = false;
        PenguinNetwork.sendToServer(new PacketInviteNPC(npc.getRegistryName()));
    }
}

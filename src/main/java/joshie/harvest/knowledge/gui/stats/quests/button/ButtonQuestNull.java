package joshie.harvest.knowledge.gui.stats.quests.button;

import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ButtonQuestNull extends ButtonBook<GuiStats> {
    public ButtonQuestNull(GuiStats gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "No Active Quests!");
        this.gui = gui;
        this.width = 120;
        this.height = 16;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mouseDragged(mc, mouseX, mouseY);
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawString(TextFormatting.BOLD + displayString, x + 8, y - 4, 0x857754);
            mc.fontRenderer.setUnicodeFlag(flag);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}

package joshie.harvest.knowledge.gui.stats.notes.button;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.notes.page.PageNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ButtonNote extends ButtonBook {
    private final GuiStats gui;
    private final Note note;
    private String title;
    private int xNote;
    private boolean unlocked;

    @SuppressWarnings("unchecked")
    public ButtonNote(GuiStats gui, Note note, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.note = note;
        this.title = note.isSecret() ? TextFormatting.AQUA + note.getTitle() : note.getTitle();
        this.unlocked = HFTrackers.getClientPlayerTracker().getTracking().getLearntNotes().contains(note.getResource());
        this.xNote = !unlocked ? 32: note.isSecret() ? 16 : 0;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            mouseDragged(mc, mouseX, mouseY);
            if (PageNotes.note.equals(note)) drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x559C8C63);
            else if (!hovered || !unlocked) drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x55B0A483);
            else drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x55C4B9A2);
            drawForeground();
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn)  {
        if (unlocked) super.playPressSound(soundHandlerIn);
    }

    private void drawForeground() {
        GlStateManager.color(1F, 1F, 1F);
        gui.mc.getTextureManager().bindTexture(HFModInfo.ICONS);
        gui.drawTexturedModalRect(xPosition, yPosition, xNote, 32, 16, 16);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (unlocked) {
            PageNotes.setNote(note);
            NoteRender render = note.getRender();
            if (render != null) {
                render.initRender(gui.mc, gui, gui.guiLeft, gui.guiTop);
            }
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (hovered && unlocked)  gui.addTooltip(title);
    }
}

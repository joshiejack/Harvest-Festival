package joshie.harvest.knowledge.gui.stats.notes.button;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.knowledge.gui.stats.notes.page.PageNotes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ButtonNote extends ButtonBook {
    private static final ItemStack NOTE_SECRET = new ItemStack(Items.GOLD_INGOT);
    private static final ItemStack NOTE_NORMAL = new ItemStack(Items.IRON_INGOT);
    private final GuiStats gui;
    private final Note note;
    private String title;
    private ItemStack stack;
    private boolean unlocked;

    @SuppressWarnings("unchecked")
    public ButtonNote(GuiStats gui, Note note, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.note = note;
        this.title = note.isSecret() ? TextFormatting.AQUA + note.getTitle() : note.getTitle();
        this.stack = note.isSecret() ? NOTE_SECRET : NOTE_NORMAL;
        this.unlocked = HFTrackers.getClientPlayerTracker().getTracking().getLearntNotes().contains(note.getResource());
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
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
        if (!unlocked) {
            StackRenderHelper.drawGreyStack(stack, xPosition, yPosition, 1F);
        } else StackRenderHelper.drawStack(stack, xPosition, yPosition, 1F);
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

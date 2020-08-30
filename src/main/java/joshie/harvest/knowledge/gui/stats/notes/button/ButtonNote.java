package joshie.harvest.knowledge.gui.stats.notes.button;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.notes.page.PageNotes;
import joshie.harvest.player.packet.PacketMarkRead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class ButtonNote extends ButtonBook {
    private final GuiStats gui;
    private final Note note;
    private final String title;
    private final boolean unlocked;
    private boolean read;

    @SuppressWarnings("unchecked")
    public ButtonNote(GuiStats gui, Note note, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.note = note;
        this.title = note.isSecret() ? TextFormatting.AQUA + note.getTitle() : note.getTitle();
        this.unlocked = HFTrackers.getClientPlayerTracker().getTracking().getLearntNotes().contains(note.getResource());
        this.read = HFTrackers.getClientPlayerTracker().getTracking().getReadStatus().contains(note.getResource());
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mouseDragged(mc, mouseX, mouseY);
            if (PageNotes.note.equals(note)) drawRect(x, y, x + width, y + height, 0x559C8C63);
            else if (!hovered || !unlocked) drawRect(x, y, x + width, y + height, 0x55B0A483);
            else drawRect(x, y, x + width, y + height, 0x55C4B9A2);
            drawForeground(mc);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn)  {
        if (unlocked) super.playPressSound(soundHandlerIn);
    }

    private void drawForeground(@Nonnull Minecraft mc) {
        if (note.getGuiResource() != null) {
            GlStateManager.color(1F, 1F, 1F);
            gui.mc.getTextureManager().bindTexture(note.getGuiResource());
            if (unlocked) gui.drawTexturedModalRect(x, y, note.getGuiX(), note.getGuiY(), 16, 16);
            else gui.drawTexturedModalRect(x, y, note.getGuiX() + 16, note.getGuiY(), 16, 16);
        } else if (note.getIcon() == Note.PAPER) {
            GlStateManager.color(1F, 1F, 1F);
            gui.mc.getTextureManager().bindTexture(HFModInfo.ICONS);
            int xNote = !unlocked ? 32: note.isSecret() ? 16 : 0;
            gui.drawTexturedModalRect(x, y, xNote, 32, 16, 16);
        } else {
            if (unlocked) StackRenderHelper.drawStack(note.getIcon(), x, y, 1F);
            else StackRenderHelper.drawGreyStack(note.getIcon(), x, y, 1F);
        }

        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        //Draw new over the icon
        if (unlocked && !read) {
            boolean unicode = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            gui.drawString(mc.fontRenderer, TextFormatting.BOLD + "NEW", x + 1, y, 0xFFFFFF);
            mc.fontRenderer.setUnicodeFlag(unicode);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        //Now we update the rest
        if (unlocked) {
            //Mark this note as having been read
            if (!read) {
                read = true; //Mark as read to update immediately
                HFTrackers.getClientPlayerTracker().getTracking().getReadStatus().add(note.getResource());
                PacketHandler.sendToServer(new PacketMarkRead(note.getResource()));
            }

            //Change the page
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

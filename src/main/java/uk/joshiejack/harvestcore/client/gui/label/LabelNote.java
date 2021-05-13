package uk.joshiejack.harvestcore.client.gui.label;

import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class LabelNote extends LabelBook {
    private Chatter chatter;
    private Interpreter it;
    private Note note;

    public LabelNote(GuiBook gui, int x, int y, LabelNote previous) {
        super(gui, x, y);
        if (previous != null && previous.note != null) {
            set(previous.note);
        }
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        if (note != null) { //Only draw shit if a note is selected
            this.drawCenteredString(mc.fontRenderer, TextFormatting.UNDERLINE + note.getLocalizedName(), x + 61, y - 7, 0xFFFFFF);
            GlStateManager.disableDepth();
            GlStateManager.pushMatrix();
            float scale = 1F;
            GlStateManager.scale(scale, scale, scale);
            boolean unicode = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            if (chatter != null) chatter.draw(mc.fontRenderer, (int) ((float)x / scale) - 1, (int)((float)y / scale) + 2, 4210752);
            mc.fontRenderer.setUnicodeFlag(unicode);
            GlStateManager.popMatrix();
            if (it != null) {
                it.callFunction("draw", this, chatter.getPage());
            }

            GlStateManager.enableDepth();
        }
    }


    public void set(Note note) {
        this.note = note; //Update the stuff
        this.it = note.getRenderScript() == null ? null : Scripting.get(note.getRenderScript());
        boolean unicode = gui.mc.fontRenderer.getUnicodeFlag();
        gui.mc.fontRenderer.setUnicodeFlag(true);
        this.chatter = new Chatter(note.getText()).withWidth(128).withLines(20).withHeight(8).withFormatting(null).setInstant();
        this.chatter.update(gui.mc.fontRenderer);
        gui.mc.fontRenderer.setUnicodeFlag(unicode);
        if (!note.init() && it != null) {
            it.callFunction("init", this);
        }
    }

    public void onForward() {
        chatter.mouseClicked(0);
    }

    public void onBack() {
        chatter.mouseClicked(1);
    }

    public Note getNote() {
        return note;
    }
}

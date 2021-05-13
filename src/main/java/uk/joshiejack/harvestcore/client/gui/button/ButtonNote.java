package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.client.gui.page.PageNotes;
import uk.joshiejack.harvestcore.network.PacketSyncPlayerData;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.util.Strings;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class ButtonNote extends ButtonBook {
    public static final ResourceLocation ICONS = GuiElements.getTexture(HarvestCore.MODID, "icons");
    private final PageNotes page;
    private final Note note;
    private boolean read;

    public ButtonNote(GuiBook guide, PageNotes page, Note note, int buttonId, int x, int y) {
        super(guide, buttonId, x, y, Strings.EMPTY);
        this.page = page;
        this.note = note;
        this.width = 16;
        this.height = 16;
        this.enabled = note.isUnlocked(guide.mc.player);
        this.read = note.isRead(guide.mc.player);
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.pushMatrix();
           // GlStateManager.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            if (note.equals(page.getNote())) drawRect(x, y, x + width, y + height, 0x559C8C63);
            else if (!hovered || !enabled) drawRect(x, y, x + width, y + height, 0x55B0A483);
            else drawRect(x, y, x + width, y + height, 0x55C4B9A2);
            GlStateManager.color(1F, 1F, 1F);
            //Complicated textures :D
            if (note.getIcon() == null) {
                //If we have the default, we use the note system
                gui.mc.getTextureManager().bindTexture(ICONS);
                if (enabled) gui.drawTexturedModalRect(x, y, (note.isHidden() ? 16 : 0), 32, 16, 32);
                else gui.drawTexturedModalRect(x, y, 32, 32, 16, 16);
            } else if (note.getIcon().getTexture() != null) {
                //Don't use the note system here
                gui.mc.getTextureManager().bindTexture(note.getIcon().getTexture());
                if (enabled) gui.drawTexturedModalRect(x, y, (note.isHidden() ? note.getIcon().getX() + 16 : note.getIcon().getX()), note.getIcon().getY(), 16, 16);
                else gui.drawTexturedModalRect(x, y, note.getIcon().getX() + 16, note.getIcon().getY(), 16, 16);
            } else drawStack(enabled, note.getIcon().getStack(), 0, 0, 1F);

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering

            if (enabled && !read) {
                boolean unicode = mc.fontRenderer.getUnicodeFlag();
                mc.fontRenderer.setUnicodeFlag(true);
                mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "NEW", x + 1, y, 0xFFFFFF);
                mc.fontRenderer.setUnicodeFlag(unicode);
            }

            if (hovered && enabled)  gui.addTooltip(note.getLocalizedName());
           // GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    private void drawIcon(ItemStack stack) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (enabled) {
            page.setNote(note);
            if (!read) {
                read = true;
                note.read(gui.mc.player); //Mark it as read by the player
                PenguinNetwork.sendToServer(new PacketSyncPlayerData(gui.mc.player, "NotesRead"));
            }
        }
    }
}

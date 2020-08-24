package joshie.harvest.api.knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.NumberFormat;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public abstract class NoteRender {
    protected static final ResourceLocation BOOK = new ResourceLocation("harvestfestival", "textures/gui/book_cooking_left.png");
    protected static final ResourceLocation ELEMENTS = new ResourceLocation("harvestfestival", "textures/gui/gui_elements.png");
    protected Minecraft mc;
    protected GuiScreen gui;
    protected int guiLeft;
    protected int guiTop;

    /** Called on initGui to setup some values
     *
     * @param mc        minecraft instance
     * @param gui       the gui screen
     * @param guiLeft   the guiLeft
     * @param guiTop    the guiTop  */
    public void initRender(Minecraft mc, GuiScreen gui, int guiLeft, int guiTop) {
        this.mc = mc;
        this.gui = gui;
        this.guiLeft = guiLeft + 164; //To align the page with the render on the right
        this.guiTop = guiTop + 20; //To align the page with the top of the page render
    }

    /** If we have init the correctly **/
    public boolean isInit() {
        return mc != null;
    }

    /** Called when rendering notes
     * @param x         the x that gets passed to drawScreen
     * @param y         the y that gets passed to drawScreen*/
    public abstract void drawScreen(int x, int y);

    /** Draws gold value -> at the coordinates
     * @param x     x position
     * @param y     y position*/
    protected void drawGold(long amount, int x, int y) {
        GlStateManager.color(1F, 1F, 1F); //Fix colours
        String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(amount);
        mc.getTextureManager().bindTexture(ELEMENTS);
        gui.drawTexturedModalRect(guiLeft + x, guiTop + y, 244, 0, 12, 12);
        drawString(text, x + 15, y + 2);
    }

    /** Draws an arrow -> at the coordinates
     * @param x     x position
     * @param y     y position*/
    protected void drawArrow(int x, int y) {
        GlStateManager.color(1F, 1F, 1F); //Fix colours
        mc.getTextureManager().bindTexture(BOOK);
        gui.drawTexturedModalRect(guiLeft + x, guiTop + y, 0, 224 + 11, 15, 10);
    }

    /** Draws a string with unicode render -> at the coordinates
     * @param x     x position
     * @param y     y position*/
    protected void drawString(String string, int x, int y) {
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        mc.fontRenderer.drawString(TextFormatting.BOLD + string, guiLeft + x, guiTop + y, 0x857754);
        mc.fontRenderer.setUnicodeFlag(flag);
    }
}
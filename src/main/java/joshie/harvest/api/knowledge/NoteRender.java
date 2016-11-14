package joshie.harvest.api.knowledge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class NoteRender {
    /** Called when rendering notes
     *
     * @param mc        minecraft instance
     * @param gui       the gui screen
     * @param x         the x that gets passed to drawScreen
     * @param y         the y that gets passed to drawScreen
     * @param guiLeft   the guiLeft
     * @param guiTop    the guiTop  */
    public abstract void drawScreen(Minecraft mc, GuiScreen gui, int x, int y, int guiLeft, int guiTop);
}

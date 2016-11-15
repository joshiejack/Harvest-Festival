package joshie.harvest.knowledge.gui.stats.notes.render;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.knowledge.NoteRender;
import joshie.harvest.core.helpers.RenderHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.tools.HFTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class NoteRenderCursedTools extends NoteRender {
    @Override
    public void drawScreen(Minecraft mc, GuiScreen gui, int x, int y, int guiLeft, int guiTop) {
        RenderHelper.drawStack(HFFishing.FISHING_ROD.getStack(ToolTier.CURSED), guiLeft + 165, guiTop + 150, 1F);
        RenderHelper.drawStack(HFTools.HOE.getStack(ToolTier.CURSED), guiLeft + 185, guiTop + 150, 1F);
        RenderHelper.drawStack(HFTools.AXE.getStack(ToolTier.CURSED), guiLeft + 205, guiTop + 150, 1F);
        RenderHelper.drawStack(HFTools.HAMMER.getStack(ToolTier.CURSED), guiLeft + 225, guiTop + 150, 1F);
        RenderHelper.drawStack(HFTools.WATERING_CAN.getStack(ToolTier.CURSED), guiLeft + 245, guiTop + 150, 1F);
        RenderHelper.drawStack(HFTools.SICKLE.getStack(ToolTier.CURSED), guiLeft + 265, guiTop + 150, 1F);
    }
}

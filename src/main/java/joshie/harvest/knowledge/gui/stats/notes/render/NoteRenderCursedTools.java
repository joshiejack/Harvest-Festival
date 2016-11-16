package joshie.harvest.knowledge.gui.stats.notes.render;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.tools.HFTools;

public class NoteRenderCursedTools extends NoteRenderHF {
    @Override
    public void drawScreen(int x, int y) {
        drawStack(HFFishing.FISHING_ROD.getStack(ToolTier.CURSED), 1, 130);
        drawStack(HFTools.HOE.getStack(ToolTier.CURSED), 21, 130);
        drawStack(HFTools.AXE.getStack(ToolTier.CURSED), 41, 130);
        drawStack(HFTools.HAMMER.getStack(ToolTier.CURSED), 61, 130);
        drawStack(HFTools.WATERING_CAN.getStack(ToolTier.CURSED), 81, 130);
        drawStack(HFTools.SICKLE.getStack(ToolTier.CURSED), 101, 130);
    }
}

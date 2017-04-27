package joshie.harvest.knowledge.gui.stats.notes.render;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.tools.HFTools;

public class NoteRenderCursedTools extends NoteRenderHF {
    @Override
    public void drawScreen(int x, int y) {
        drawStack(HFFishing.FISHING_RODS.get(ToolTier.CURSED).getStack(), 1, 130);
        drawStack(HFTools.HOES.get(ToolTier.CURSED).getStack(), 21, 130);
        drawStack(HFTools.AXES.get(ToolTier.CURSED).getStack(), 41, 130);
        drawStack(HFTools.HAMMERS.get(ToolTier.CURSED).getStack(), 61, 130);
        drawStack(HFTools.WATERING_CANS.get(ToolTier.CURSED).getStack(), 81, 130);
        drawStack(HFTools.SICKLES.get(ToolTier.CURSED).getStack(), 101, 130);
    }
}

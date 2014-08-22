package harvestmoon.gui.feature;

import harvestmoon.gui.GuiBase;
import harvestmoon.lib.ModInfo;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.StatCollector;

public class Feature {
    private TextureManager tm;

    public Feature() {
        tm = Minecraft.getMinecraft().renderEngine;
    }

    public void draw(GuiBase gui, int x, int y, int mouseX, int mouseY) {
        tm.bindTexture(ModInfo.elements);
    }

    public void addTooltip(List tooltip, int mouseX, int mouseY) {
        return;
    }

    public void mouseClicked(int mouseX, int mouseY) {
        return;
    }

    public void addLine(String start, int i, String note, List tooltip) {
        String line = start + "." + note.toLowerCase().replaceAll("_", "\\.") + ".text" + (i + 1);
        if (!line.equals(StatCollector.translateToLocal(line))) {
            tooltip.add(StatCollector.translateToLocal(line));
        }
    }
}

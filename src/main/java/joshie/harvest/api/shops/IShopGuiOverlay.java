package joshie.harvest.api.shops;

import net.minecraft.client.gui.GuiScreen;

public interface IShopGuiOverlay {
    void renderOverlay(GuiScreen gui, int x, int y, int xSize, int ySize);
}
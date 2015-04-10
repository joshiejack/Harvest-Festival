package joshie.harvestmoon.api.shops;

import net.minecraft.client.gui.GuiScreen;

public interface IShopGuiOverlay {
    public void renderOverlay(GuiScreen gui, int x, int y, int xSize, int ySize);
}

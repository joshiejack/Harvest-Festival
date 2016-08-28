package joshie.harvest.api.shops;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IShopGuiOverlay {
    void renderOverlay(GuiScreen gui, int x, int y, int xSize, int ySize);
}
package joshie.harvestmoon.core.gui;

import joshie.harvestmoon.blocks.tiles.TileRuralChest;
import joshie.harvestmoon.core.util.Translate;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiRuralChest extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private final TileRuralChest tile;

    public GuiRuralChest(InventoryPlayer player, TileRuralChest tile) {
        super(new ContainerRuralChest(tile, player));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString(Translate.translate(tile.getInventoryName()), 42, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}

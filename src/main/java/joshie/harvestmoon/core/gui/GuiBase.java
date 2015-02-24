package joshie.harvestmoon.core.gui;

import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;

import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiBase extends GuiContainer {
    protected boolean hasInventory;
    private String name;
    private static ResourceLocation TEXTURE;
    private int nameHeight = 5;
    private int inventOffset = 3;
    protected int mouseX = 0;
    protected int mouseY = 0;
    private ArrayList<String> tooltip = new ArrayList<String>();

    public GuiBase(ContainerBase container, String texture, int yOffset) {
        super(container);
        TEXTURE = new ResourceLocation(MODPATH, "textures/gui/" + texture + ".png");
        ySize += yOffset;
        xSize = 201;
        name = "";
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        drawForeground(x, y);
        if (hasInventory) {
            fontRendererObj.drawString(getName(), getX(), nameHeight, 4210752);
            fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + inventOffset, 4210752);
        }

        tooltip.clear();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        drawBackground(f, i, j);
        drawBackground(x, y);
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return 46;
    }

    public void drawForeground(int x, int y) {
        return;
    }

    public void drawBackground(float f, int x, int y) {
        return;
    }

    public void drawBackground(int x, int y) {
        return;
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        onMouseClick(mouseX, mouseY);
    }

    protected void onMouseClick(int mouseX, int mouseY) {
        return;
    }
}

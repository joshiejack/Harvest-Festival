package uk.joshiejack.penguinlib.client.gui;

import uk.joshiejack.penguinlib.inventory.ContainerPenguin;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiPenguinContainer extends GuiContainer {
    protected boolean hasInventory;
    private static final int nameHeight = 5;
    private static final int inventOffset = 3;
    private final ResourceLocation texture;
    private final ArrayList<String> tooltip = new ArrayList<>();
    private final String name;
    protected int mouseWheel;

    public GuiPenguinContainer(ContainerPenguin container, ResourceLocation resource, int yOffset) {
        super(container);
        texture = resource;
        ySize += yOffset;
        xSize = 201;
        name = "";
    }

    public FontRenderer getFont() {
        return fontRenderer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)  {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTooltip(tooltip, mouseX, mouseY);
        tooltip.clear();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        drawForeground(x, y);
        if (hasInventory) {
            fontRenderer.drawString(getName(), getX(), nameHeight, 4210752);
            fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + inventOffset, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        if (texture != null) {
            mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        }

        drawBackground(x, y);

    }

    public String getName() {
        return name;
    }

    public int getX() {
        return 46;
    }

    public void drawForeground(int x, int y) {}
    public void drawBackground(int x, int y) {}

    public void addTooltip(List<String> list) {
        tooltip.addAll(list);
    }

    protected void drawTooltip(List<String> list, int x, int y) {
        if (!list.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int k = 0;

            for (String s : list) {
                int l = fontRenderer.getStringWidth(s);
                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }

            if (j2 + k > this.width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height) {
                k2 = this.height - i1 - 6;
            }

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = 0xEE1F1F1F;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 0xEE504D4C;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < list.size(); ++i2) {
                String s1 = list.get(i2);
                fontRenderer.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        mouseWheel = Mouse.getDWheel();
        super.handleMouseInput();
    }
}

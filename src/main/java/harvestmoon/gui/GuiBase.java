package harvestmoon.gui;

import static harvestmoon.lib.ModInfo.MODID;
import harvestmoon.gui.feature.Feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiBase extends GuiContainer {
    private String name;
    private static ResourceLocation TEXTURE;
    private int nameHeight = 5;
    private int inventOffset = 3;
    private int mouseX = 0;
    private int mouseY = 0;
    private ArrayList<String> tooltip = new ArrayList<String>();
    protected ArrayList<Feature> features = new ArrayList<Feature>();

    public GuiBase(ContainerBase container, String texture, int yOffset) {
        super(container);
        TEXTURE = new ResourceLocation(MODID, "textures/gui/" + texture + ".png");
        ySize += yOffset;
        xSize = 201;
        name = "";
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        drawForeground();
        fontRendererObj.drawString(getName(), getX(), nameHeight, 4210752);
        fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + inventOffset, 4210752);
        tooltip.clear();
        addToolTips();
        drawToolTip(tooltip, mouseX, mouseY, fontRendererObj);
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

        if (features.size() > 0) {
            drawFeatures(x, y);
        }
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return 46;
    }

    public void drawForeground() {
        return;
    }

    public void drawBackground(float f, int x, int y) {
        return;
    }

    public void drawBackground(int x, int y) {
        return;
    }

    private void drawFeatures(int x, int y) {
        for (Feature feat : features) {
            feat.draw(this, x, y, mouseX, mouseY);
        }
    }

    private void addToolTips() {
        for (Feature feat : features) {
            feat.addTooltip(tooltip, mouseX, mouseY);
        }

        addToolTip();
    }

    public void addToolTip() {
        return;
    }

    public void addItemToolTip(ItemStack stack, List<String> list) {
        return;
    }

    private void drawToolTip(List list, int x, int y, FontRenderer font) {
        if (!list.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int i1 = x + 12;
            int j1 = y - 12;
            int k1 = 8;

            if (list.size() > 1) {
                k1 += 2 + (list.size() - 1) * 10;
            }

            if (i1 + k > width) {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > height) {
                j1 = height - k1 - 6;
            }

            zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < list.size(); ++k2) {
                String s1 = (String) list.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0) {
                    j1 += 2;
                }

                j1 += 10;
            }

            zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
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
        for (Feature feat : features) {
            feat.mouseClicked(mouseX, mouseY);
        }

        onMouseClick(mouseX, mouseY);
    }

    protected void onMouseClick(int mouseX, int mouseY) {
        return;
    }
}

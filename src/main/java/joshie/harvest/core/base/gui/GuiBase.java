package joshie.harvest.core.base.gui;

import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npcs.gui.ContainerNPCChat;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GuiBase extends GuiScreen {
    private Container container;
    protected boolean hasInventory;
    private ResourceLocation TEXTURE;
    private static final int nameHeight = 5;
    private static final int inventOffset = 3;
    private final ArrayList<String> tooltip = new ArrayList<>();
    private final String name;
    public int xSize = 176;
    public int ySize = 166;
    protected int guiLeft;
    protected int guiTop;
    protected int mouseX = 0;
    protected int mouseY = 0;
    protected int mouseWheel;

    public GuiBase(ContainerBase container, String texture, int yOffset) {
        this.container = container;
        TEXTURE = new ResourceLocation(HFModInfo.MODID, "textures/gui/" + texture + ".png");
        ySize += yOffset;
        xSize = 201;
        name = "";
    }

    public FontRenderer getFont() {
        return fontRendererObj;
    }

    @Override
    public void initGui() {
        mc.thePlayer.openContainer = container;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    @Override
    public void onGuiClosed() {
        if (container instanceof ContainerNPCChat) {
            container.onContainerClosed(MCClientHelper.getPlayer());
        }
    }

    protected void drawGuiTexture() {
        mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawGuiTexture();
        drawBackground(guiLeft, guiTop);
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTooltip(tooltip, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)guiLeft,(float)guiTop, 0.0F);
        GlStateManager.enableRescaleNormal();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawForeground(mouseX, mouseY);
        if (hasInventory) {
            fontRendererObj.drawString(getName(), getX(), nameHeight, 4210752);
            fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + inventOffset, 4210752);
        }

        tooltip.clear();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
        pointX = pointX - guiLeft;
        pointY = pointY - guiTop;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return 46;
    }

    public void drawForeground(int x, int y) {}
    public void drawBackground(int x, int y) {}

    public void addTooltip(String string) {
        tooltip.add(string);
    }
    
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
            Iterator<String> iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = iterator.next();
                int l = fontRendererObj.getStringWidth(s);

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
                fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);

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
        int x = Mouse.getEventX() * width / mc.displayWidth;
        int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        mouseX = x - guiLeft;
        mouseY = y - guiTop;
        mouseWheel = Mouse.getDWheel();
        super.handleMouseInput();
    }
}

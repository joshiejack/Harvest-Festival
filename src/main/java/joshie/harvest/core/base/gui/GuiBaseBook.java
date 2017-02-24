package joshie.harvest.core.base.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiBaseBook extends GuiScreen {

    private final ArrayList<Runnable> runnables = new ArrayList<>();
    private final ArrayList<String> tooltip = new ArrayList<>();
    private final ResourceLocation left;
    private final ResourceLocation right;
    private final int backgroundWidth;
    private final int backgroundHeight;
    private static BookPage page;
    private int xSize = 176;
    private int ySize = 166;
    public int guiLeft;
    public int guiTop;

    public GuiBaseBook(ResourceLocation left, ResourceLocation right, int backgroundWidth, int backgroundHeight) {
        this.left = left;
        this.right = right;
        this.xSize = backgroundWidth * 2;
        this.ySize = backgroundHeight;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        GuiBaseBook.page = page != null ? page : getDefaultPage();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        buttonList.clear();
        labelList.clear();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        page.initGui(this, buttonList, labelList); //Reinit the gui
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        int centreX = (width / 2) - backgroundWidth;
        int centreY = (height - backgroundHeight) / 2;
        mc.getTextureManager().bindTexture(right);
        drawTexturedModalRect(centreX + backgroundWidth, centreY, 0, 0, backgroundWidth, backgroundHeight);
        mc.getTextureManager().bindTexture(left);
        drawTexturedModalRect(centreX, centreY, 102, 0, backgroundWidth, backgroundHeight);
        runnables.clear();
        tooltip.clear();
        super.drawScreen(x, y, partialTicks);
        page.drawScreen(x, y);
        drawTooltip(tooltip, x, y);
        runnables.forEach(Runnable :: run);
    }

    public BookPage getPage() {
        return GuiBaseBook.page;
    }

    public void setPage(BookPage page) {
        GuiBaseBook.page = page;
        this.initGui();
    }

    public void addTooltip(String string) {
        tooltip.add(string);
    }

    public void addTooltip(List<String> list) {
        tooltip.addAll(list);
    }

    public abstract BookPage getDefaultPage();

    private void drawTooltip(List<String> list, int x, int y) {
        if (!list.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int k = 0;

            for (String s : list) {
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

            if (j2 + k > width) {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > height) {
                k2 = height - i1 - 6;
            }

            zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = 0xEE1F1F1F;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 0xEE504D4C;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < list.size(); ++i2) {
                String s1 = list.get(i2);
                fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}

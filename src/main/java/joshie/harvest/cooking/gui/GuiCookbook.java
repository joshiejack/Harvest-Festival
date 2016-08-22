package joshie.harvest.cooking.gui;

import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiCookbook extends GuiScreen {
    public static final ResourceLocation LEFT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_left.png");
    public static final ResourceLocation RIGHT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_right.png");
    public static final PageUtensilList MASTER = new PageUtensilList();
    private static final int imageWidth = 154;
    private static final int imageHeight = 204;

    private static Page page;
    private int centreX;
    private int centreY;

    public GuiCookbook() {
        setPage(page == null ? MASTER : page);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        centreX = (width / 2) - imageWidth;
        centreY = (height - imageHeight) / 2;
        int mouseX = x -centreX;
        int mouseY = y - centreY;
        mc.getTextureManager().bindTexture(RIGHT_GUI);
        drawTexturedModalRect(centreX + imageWidth, centreY, 0, 0, imageWidth, imageHeight);
        mc.getTextureManager().bindTexture(LEFT_GUI);
        drawTexturedModalRect(centreX, centreY, 102, 0, imageWidth, imageHeight);
        page.draw(mouseX, mouseY);
        mc.getTextureManager().bindTexture(LEFT_GUI);

        if (page.getOwner() != page) {
            GlStateManager.color(1F, 1F, 1F);
            int buttonY = mouseX >= 24 && mouseX <= 39 && mouseY >= 168 && mouseY <= 178 ? 246 : 235;
            drawTexture(24, 168, 16, buttonY, 15, 10);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        int mouseX = x -centreX;
        int mouseY = y - centreY;
        page.mouseClicked(mouseX, mouseY);
        if (page.getOwner() != page && mouseX >= 24 && mouseX <= 39 && mouseY >= 168 && mouseY <= 178) {
            setPage(page.getOwner());
        }
    }

    public void setPage(Page page) {
        GuiCookbook.page = page.initGui(this);
    }

    public void drawString(int x, int y, String text) {
        fontRendererObj.drawString(text, centreX + x, centreY + y, 4210752);
    }

    public void drawStack(int x, int y, ItemStack stack, float scale) {
        StackHelper.drawStack(stack, centreX + x, centreY + y, scale);
    }

    public void drawTexture(int x, int y, int startX, int startY, int widthX, int heightY) {
        drawTexturedModalRect(centreX + x, centreY + y, startX, startY, widthX, heightY);
    }
}

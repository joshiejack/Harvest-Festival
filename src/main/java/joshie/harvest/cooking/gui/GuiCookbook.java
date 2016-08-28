package joshie.harvest.cooking.gui;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.helpers.generic.StackHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GuiCookbook extends GuiScreen {
    public static final ResourceLocation LEFT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_left.png");
    public static final ResourceLocation RIGHT_GUI = new ResourceLocation(HFModInfo.MODID, "textures/gui/book_cooking_right.png");
    public static final PageUtensilList MASTER = new PageUtensilList();
    public static final Set<Ingredient> ingredients = new HashSet<>();
    private static final int imageWidth = 154;
    private static final int imageHeight = 204;
    static final int MAX_UTENSILS_DISPLAY = 5;

    private static Page page;
    private int centreX;
    private int centreY;

    public GuiCookbook() {
        setPage(page == null ? MASTER : page);
        ingredients.clear();
        for (ItemStack stack: MCClientHelper.getPlayer().inventory.mainInventory) {
            if (stack != null) {
                ingredients.addAll(CookingAPI.INSTANCE.getCookingComponents(stack));
            }
        }

        ingredients.remove(null); //Remove any nulls
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
        //Draw the utensil buttons
        if (page.getUtensil() != null) {
            //Draw the background buttons
            GlStateManager.color(1F, 1F, 1F);
            boolean hoverX = mouseX >= 307 && mouseX <= 333;
            for (int i = 0; i < MAX_UTENSILS_DISPLAY; i++) {
                Utensil tool = Utensil.getUtensilFromIndex(i);
                if (PageRecipeList.get(tool).hasRecipes()) {
                    mc.getTextureManager().bindTexture(LEFT_GUI);
                    boolean hoverY = mouseY >= 16 + i * 36 && mouseY <= 50 + i * 36;
                    int theY = page.getUtensil() == tool ? 66 : hoverX && hoverY ? 33: 0;
                    drawTexture(308, 16 + i * 36, 0, theY, 26, 34);
                    drawStack(308, 25 + i * 36, PageRecipeList.get(tool).getItem(), 1F);
                }
            }
        }

        mc.getTextureManager().bindTexture(LEFT_GUI);
        //Draw the back button
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
        if (page.mouseClicked(mouseX, mouseY)) return;
        //Draw the utensil buttons
        if (page.getUtensil() != null) {
            boolean hoverX = mouseX >= 307 && mouseX <= 333;
            for (int i = 0; i < MAX_UTENSILS_DISPLAY; i++) {
                Utensil tool = Utensil.getUtensilFromIndex(i);
                if (PageRecipeList.get(tool).hasRecipes()) {
                    boolean hoverY = mouseY >= 16 + i * 36 && mouseY <= 50 + i * 36;
                    if (hoverX && hoverY) {
                        setPage(PageRecipeList.get(tool));
                        return; //Don't continue
                    }
                }
            }
        }


        if (page.getOwner() != page && mouseX >= 24 && mouseX <= 39 && mouseY >= 168 && mouseY <= 178 || mouseButton == 1) {
            setPage(page.getOwner());
        }
    }

    public boolean setPage(Page page) {
        GuiCookbook.page = page.initGui(this);
        return true;
    }

    public void drawString(int x, int y, String text) {
        fontRendererObj.drawSplitString(text, centreX + x, centreY + y, 120, 4210752);
    }

    public void drawStack(int x, int y, ItemStack stack, float scale) {
        StackHelper.drawStack(stack, centreX + x, centreY + y, scale);
    }

    public void drawTexture(int x, int y, int startX, int startY, int widthX, int heightY) {
        drawTexturedModalRect(centreX + x, centreY + y, startX, startY, widthX, heightY);
    }

    public void drawBox(int x, int y, int width, int length, int color) {
        drawRect(centreX + x, centreY + y, centreX + x + width, centreY + y + length, color);
    }
}

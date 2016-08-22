package joshie.harvest.cooking.gui;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.util.Text;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.cooking.gui.GuiCookbook.LEFT_GUI;

/** Display the utensils **/
public class PageUtensilList extends Page {
    private List<PageRecipeList> pages;

    @Override
    public PageUtensilList initGui(GuiCookbook gui) {
        super.initGui(gui);
        pages = new ArrayList<>();
        for (Utensil utensil: Utensil.values()) {
            PageRecipeList page = PageRecipeList.get(utensil);
            if (page.initGui(gui).hasRecipes()) {
                pages.add(page);
            }
        }

        return this;
    }

    @Override
    public Page getOwner() {
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        boolean hoverX = mouseX >= 166 && mouseX <= 288;
        gui.drawString(20, 20, Text.translate("cookbook"));
        gui.drawString(205, 13, TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + Text.translate("utensils"));
        for (int i = 0; i < pages.size(); i++) {
            ItemStack stack = pages.get(i).getItem();
            boolean hoverY = mouseY >= 21 + 31 * i && mouseY <= 21 + 31 * i + 30;
            if (hoverX && hoverY) {
                GlStateManager.color(1F, 1F, 1F);
                gui.mc.getTextureManager().bindTexture(LEFT_GUI);
                gui.drawTexture(163, 23 + i * 31, 131, 222, 124, 33);
                gui.drawString(202, 37 + i * 31, TextFormatting.ITALIC + stack.getDisplayName());
            } else gui.drawString(202, 37 + i * 31, stack.getDisplayName());
            gui.drawStack(167, 26 + i * 31, stack, 1.9F);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        boolean hoverX = mouseX >= 166 && mouseX <= 288;
        for (int i = 0; i < pages.size(); i++) {
            boolean hoverY = mouseY >= 21 + 31 * i && mouseY <= 21 + 31 * i + 30;
            if (hoverX && hoverY) {
                gui.setPage(pages.get(i));
            }
        }
    }
}

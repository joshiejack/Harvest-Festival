package uk.joshiejack.gastronomy.client.gui.buttons;

import uk.joshiejack.gastronomy.client.gui.pages.PageRecipe;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.IngredientStack;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ButtonRecipeListing extends ButtonBook {
    private final PageRecipe page;
    private final Recipe recipe;
    private final ItemStack stack;
    private final boolean isInInventory;

    public ButtonRecipeListing(PageRecipe page, GuiBook gui, int buttonId, int x, int y, String string, NonNullList<ItemStack> stacks, List<FluidStack> fluids) {
        super(gui, buttonId, x, y, string);
        this.height = 14;
        this.width = 130;
        this.page = page;
        this.recipe = page.getRecipe();
        this.stack = this.recipe.getResult();
        boolean all = true;
        for (IngredientStack ingredient: recipe.getRequired()) {
            if (!Cooker.isInInventories(stacks, fluids, ingredient)) {
                all = false;
                break;
            }
        }

        this.isInInventory = all;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            drawStack(stack, 8, 0, 1F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                mc.fontRenderer.drawString(TextFormatting.ITALIC + stack.getDisplayName(), x + 28, y + 4, 4210752);
            } else mc.fontRenderer.drawString(stack.getDisplayName(), x + 28, y + 4, 4210752);

            GlStateManager.disableDepth();
            TextFormatting formatting = isInInventory ? TextFormatting.DARK_GREEN : TextFormatting.RED;
            mc.fontRenderer.drawString(formatting + "", x + 21, y + 7, 4210752);
            gui.mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            if (isInInventory) {
                drawTexturedModalRect(x + 16, y + 8, 31, 248, 10, 8);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(page);
    }
}

package uk.joshiejack.gastronomy.client.gui.buttons;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.gui.GuiCookbook;
import uk.joshiejack.gastronomy.client.gui.pages.PageApplianceList;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ButtonTab extends ButtonBook {
    private final Appliance appliance;
    private final ItemStack stack;
    private final boolean selected;

    public ButtonTab(Appliance appliance, boolean selected, GuiBook gui, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.appliance = appliance;
        this.stack = Cooker.getStackFromAppliance(appliance);
        this.selected = selected;
        this.width = 26;
        this.height = 32;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mc.getTextureManager().bindTexture(GuiCookbook.LEFT_GUI);
            int theY = selected ? 64 :hovered ? 32: 0;
            drawTexturedModalRect(x, y, 0, theY, 26, 32);
            drawStack(stack, 0, 8, 1F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(PageApplianceList.RECIPE_LISTS.get(appliance));
    }
}

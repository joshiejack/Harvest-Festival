package uk.joshiejack.gastronomy.client.gui.buttons;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.gui.pages.PageApplianceList;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ButtonApplianceList extends ButtonBook{
    private final Appliance appliance;
    private final ItemStack stack;

    public ButtonApplianceList(Appliance appliance, GuiBook gui, int buttonId, int x, int y, String buttonText) {
        super(gui, buttonId, x, y, buttonText);
        this.appliance = appliance;
        this.width = 125;
        this.height = 31;
        this.stack = PageApplianceList.RECIPE_LISTS.get(appliance).icon;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        //super.drawButton(mc, mouseX, mouseY, partialTicks);
        if (visible) {
            drawStack(stack, 4, 3, 1.7F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
                GlStateManager.color(1F, 1F, 1F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                drawTexturedModalRect(x, y, 131, 225, 125, 31);
                mc.fontRenderer.drawString(TextFormatting.ITALIC + stack.getDisplayName(), x + 39, y + 12, 4210752);
            } else mc.fontRenderer.drawString(stack.getDisplayName(), x + 39, y + 12, 4210752);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.setPage(PageApplianceList.RECIPE_LISTS.get(appliance));
    }
}

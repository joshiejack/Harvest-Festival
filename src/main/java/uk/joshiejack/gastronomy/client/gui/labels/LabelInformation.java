package uk.joshiejack.gastronomy.client.gui.labels;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LabelInformation extends LabelBook {
    private final String description;
    private final ItemStack stack;
    private final int hunger;

    public LabelInformation(GuiBook gui, ItemStack stack) {
        super(gui, 0, 0);
        this.stack = stack;
        this.hunger = stack.getItem() instanceof ItemFood ? ((ItemFood)stack.getItem()).getHealAmount(stack) : 0;
        this.description = stack.getTranslationKey() + ".description";
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            int left = ((36-stack.getDisplayName().length()) * 2);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + stack.getDisplayName(), x + left, y + 20, 4210752);
            drawHorizontalLine(x + 25, x + 135, y + 30, 0xFFB0A483);
            drawHorizontalLine(x + 26, x + 136, y + 31, 0xFF9C8C63);
            mc.fontRenderer.drawString(TextFormatting.BOLD + StringHelper.localize("gastronomy.restores"), x + 60, y + 35, 4210752);

            mc.fontRenderer.drawString(TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + StringHelper.localize("gastronomy.description"), x + 25, y + 65, 4210752);
            mc.fontRenderer.drawSplitString(StringHelper.localize(description), x + 25, y + 78, 120, 4210752);

            gui.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
            GlStateManager.color(1F, 1F, 1F);
            for (int i = 0; i < hunger; i++) {
                if (i % 2 == 0) {
                    drawTexturedModalRect(x + (i * 4) + 58, y + 46, 16, 27, 9, 9);
                }

                if (i + 1 < hunger && i % 2 == 0) {
                    drawTexturedModalRect(x + (i * 4) + 58, y + 46, 52, 27, 9, 9);
                } else if (i % 2 == 0) {
                    drawTexturedModalRect(x + (i * 4) + 58, y + 46, 61, 27, 9, 9);
                }
            }

            drawHorizontalLine(x + 25, x + 135, y + 60, 0xFFB0A483);
            drawHorizontalLine(x + 26, x + 136, y + 61, 0xFF9C8C63);
            drawStack(stack, 22, 30, 2F);
            //Right hand side
            drawHorizontalLine(x + 170, x + 280, y + 30, 0xFFB0A483);
            drawHorizontalLine(x + 171, x + 281, y + 31, 0xFF9C8C63);
            mc.fontRenderer.drawString(TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + StringHelper.localize("gastronomy.recipe"), x + 190, y + 20, 4210752);
        }
    }
}

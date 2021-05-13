package uk.joshiejack.harvestcore.client.gui.label;

import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.client.gui.MicroFont;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class LabelShippedItem extends LabelBook {
    private final CyclingStack stack;
    private final long value;
    private ItemStack previous;

    public LabelShippedItem(GuiBook gui, int x, int y, CyclingStack stack, long value) {
        super(gui, x, y);
        this.width = 16;
        this.height = 16;
        this.stack = stack;
        this.value = value;
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            ItemStack current = stack.getStack(0);
            if (previous == null || current != previous) {
                previous = current; //WOO, PREVIOUSSSSSSS
            }

            FontRenderer f = mc.fontRenderer;
            mc.fontRenderer = MicroFont.INSTANCE;
            drawStack(current, 0, 0, 1F);
            mc.fontRenderer = f;

            boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered) {
                gui.addTooltip(TextFormatting.WHITE + current.getDisplayName());
                gui.addTooltip(TextFormatting.GOLD + "" + NumberFormat.getNumberInstance(Locale.ENGLISH).format(value) + "G");
            }

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
        }
    }
}

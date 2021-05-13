package uk.joshiejack.harvestcore.client.gui.label;

import joptsimple.internal.Strings;
import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.harvestcore.database.Collections;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class LabelShippingCollection extends LabelBook {
    private final CyclingStack stack;
    private final Collections.Collection collection;
    private final long value;
    private ItemStack previous;
    private boolean obtained;

    public LabelShippingCollection(GuiBook gui, int x, int y, CyclingStack stack, long value, Collections.Collection collection) {
        super(gui, x, y);
        this.width = 16;
        this.height = 16;
        this.stack = stack;
        this.value = value;
        this.collection = collection;
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            ItemStack current = stack.getStack(0);
            if (previous == null || current != previous) {
                obtained = collection.isObtained(mc.player, current);
                previous = current; //WOO, PREVIOUSSSSSSS
            }

            if (obtained) {
                drawStack(current, 0, 0, 1F);
            } else drawGreyStack(current, 0, 0, 1F);

            boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            if (hovered && (obtained || !collection.isShippingCollection())) {
                TextFormatting formatting = obtained ? TextFormatting.GREEN : TextFormatting.GRAY;
                gui.addTooltip(formatting + current.getDisplayName());
                String tooltip = collection.formatValue(mc.player, current, value);
                if (!Strings.isNullOrEmpty(tooltip)) gui.addTooltip(tooltip);
                if (collection.isShippingCollection()) gui.addTooltip("(" + Shipped.getCount(current) + " Sold)");
            }

            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT); //Reset the rendering
        }
    }
}

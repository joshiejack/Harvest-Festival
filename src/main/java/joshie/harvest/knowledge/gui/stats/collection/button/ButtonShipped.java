package joshie.harvest.knowledge.gui.stats.collection.button;

import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.util.holders.AbstractItemHolder;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ButtonShipped extends ButtonBook<GuiStats> {
    private final GuiStats gui;
    protected final AbstractItemHolder holder;
    private final long value;
    private final boolean obtained;
    private int hoverTimer;
    @Nonnull
    private ItemStack stack = ItemStack.EMPTY;
    private int index = -1;

    @SuppressWarnings("unchecked")
    public ButtonShipped(GuiStats gui, AbstractItemHolder holder, long value, boolean obtained, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.holder = holder;
        this.obtained = obtained;
        this.value = value;
    }

    public ButtonShipped(GuiStats gui, ItemStack stack, long value, boolean obtained, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.holder = null;
        this.stack = stack;
        this.obtained = obtained;
        this.value = value;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mouseDragged(mc, mouseX, mouseY);
            drawForeground();
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private void updateStack() {
        if (holder == null) return;
        if (index == -1) {
            index = 0;
            stack = holder.getMatchingStacks().get(0);
        } else {
            if (holder.getMatchingStacks().size() > 1) {
                index++;
                if (index < holder.getMatchingStacks().size()) {
                    stack = holder.getMatchingStacks().get(index);
                } else {
                    index = 0;
                    stack = holder.getMatchingStacks().get(index);
                }
            }
        }
    }

    private void drawForeground() {
        if (stack.isEmpty() || hoverTimer %100 == 0) updateStack();
        hoverTimer++;
        if (!obtained) {
            StackRenderHelper.drawGreyStack(stack, x, y, 1F);
        } else StackRenderHelper.drawStack(stack, x, y, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (obtained && hovered && !stack.isEmpty()) {
            gui.addTooltip(TextFormatting.GREEN + stack.getDisplayName());
            if (value > 0L) gui.addTooltip(value + "G");
        }
    }
}

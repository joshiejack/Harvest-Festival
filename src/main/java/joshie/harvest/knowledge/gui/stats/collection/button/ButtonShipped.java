package joshie.harvest.knowledge.gui.stats.collection.button;

import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonBook;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.util.holders.AbstractItemHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ButtonShipped extends ButtonBook {
    private final GuiStats gui;
    protected final AbstractItemHolder holder;
    private final long value;
    private final boolean obtained;
    private int hoverTimer;
    private ItemStack stack;
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
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
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
        if (stack == null || hoverTimer %100 == 0) updateStack();
        hoverTimer++;
        if (!obtained) {
            StackRenderHelper.drawGreyStack(stack, xPosition, yPosition, 1F);
        } else StackRenderHelper.drawStack(stack, xPosition, yPosition, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (obtained && hovered && stack != null) {
            gui.addTooltip(TextFormatting.GREEN + stack.getDisplayName());
            if (value > 0L) gui.addTooltip(value + "G");
        }
    }
}

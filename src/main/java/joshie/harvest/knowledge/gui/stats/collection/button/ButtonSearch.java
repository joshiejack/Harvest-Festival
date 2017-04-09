package joshie.harvest.knowledge.gui.stats.collection.button;

import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.collection.page.PageCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

import static joshie.harvest.api.HFApi.shipping;

public class ButtonSearch extends ButtonBook<GuiStats> {
    public static boolean inFocus;
    private final PageCollection collection;

    @SuppressWarnings("unchecked")
    public ButtonSearch(GuiStats gui, PageCollection collection, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, "");
        this.collection = collection;
        this.width = 80;
        this.height = 24;
        //ButtonSearch.inFocus = false;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            mouseDragged(mc, mouseX, mouseY);
            mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPosition, yPosition, 0, 169, width, height);
            mc.fontRendererObj.drawString(TextFormatting.ITALIC + collection.search + (inFocus ? "_": ""), xPosition + 8, yPosition + 8, 0x391312);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    public static boolean matchesFilter(ItemStack stack, String search) {
        if (search.startsWith("=")) {
            long value = shipping.getSellValue(stack);
            try {
                return value == Long.valueOf(search.substring(1).trim());
            } catch (Exception ignored) {}
        } else if (search.startsWith("<=")) {
            long value = shipping.getSellValue(stack);
            try {
                return value <= Long.valueOf(search.substring(2).trim());
            } catch (Exception ignored) {}
        } else if (search.startsWith("<")) {
            long value = shipping.getSellValue(stack);
            try {
                return value < Long.valueOf(search.substring(1).trim());
            } catch (Exception ignored) {}
        } else if (search.startsWith(">=")) {
            long value = shipping.getSellValue(stack);
            try {
                return value >= Long.valueOf(search.substring(2).trim());
            } catch (Exception ignored) {}
        } else if (search.startsWith(">")) {
            long value = shipping.getSellValue(stack);
            try {
                return value > Long.valueOf(search.substring(1).trim());
            } catch (Exception ignored) {}
        } else if (search.startsWith("@")) {
            String modid = stack.getItem().getRegistryName().getResourceDomain().toLowerCase();
            return modid.contains(search.substring(1));
        }

        return stack.getDisplayName().toLowerCase().contains(search);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return inFocus = super.mousePressed(mc, mouseX, mouseY);
    }
}

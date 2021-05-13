package uk.joshiejack.harvestcore.client.gui.button;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.util.Strings;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.network.blueprint.PacketCraftBlueprint;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.client.gui.CyclingObject;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonBook;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class ButtonCraft extends ButtonBook {
    private Blueprint blueprint;
    private List<String> tooltip = Lists.newArrayList();
    private ItemStack render;
    private int originalCount, currentCount;

    public ButtonCraft(GuiBook guide, Blueprint blueprint, int buttonId, int x, int y) {
        super(guide, buttonId, x, y, Strings.EMPTY);
        this.blueprint = blueprint;
        this.width = 16;
        this.buildTooltip();
        this.enabled = blueprint.hasAllItems(guide.mc.player, 1);
        this.render = blueprint.getResult().copy();
        this.originalCount = this.render.getCount();
    }

    private static List<ItemStack> withSize(Collection<ItemStack> stackz, int size) {
        List<ItemStack> stacks = Lists.newArrayList();
        for (ItemStack stack: stackz) {
            ItemStack clone = stack.copy();
            clone.setCount(size);
            stacks.add(clone);
        }

        return stacks;
    }

    private void buildTooltip() {
        tooltip.clear();
        Object2IntMap<Holder> ingredients = blueprint.getRequirements();
        Object2IntMap<String> requirements = new Object2IntOpenHashMap<>();
        Multimap<String, ItemStack> map = HashMultimap.create();
        for (Holder i: ingredients.keySet()) {
            if (i.getStacks().size() > 0) {
                ItemStack stack = i.getStacks().get(0);
                String key = blueprint.getRegistryName() + "@" + StackHelper.getStringFromStack(stack);
                requirements.put(key, ingredients.get(i));
                map.get(key).addAll(Lists.newArrayList(i.getStacks()));
            } else HarvestCore.logger.error("Couldn't find an item for the holder: " + i.toString());
        }

        map.keySet().forEach(key -> GuiBook.cycles.put(key,
                new CyclingObject<>(withSize(map.get(key), getCount(requirements.get(key))), ItemStack.EMPTY)));
        tooltip.add(blueprint.getResult().getDisplayName());
        requirements.forEach((k, v) -> tooltip.add(k + "`"));
    }

    private int getCount(int originalCount) {
        int count = originalCount;
        if (GuiScreen.isShiftKeyDown()) count *= 5;
        if (GuiScreen.isCtrlKeyDown()) count *= 10;
        if (GuiScreen.isAltKeyDown()) count *= 25;
        return count;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.enableDepth();
            mc.getTextureManager().bindTexture(GuiElements.BOOK_LEFT);
            if (hovered) {
                int count = getCount(originalCount);
                render.setCount(count);

                if (count != currentCount) {
                    currentCount = count;
                    enabled = blueprint.hasAllItems(mc.player, getCount(1));
                    buildTooltip();
                }
            } else render.setCount(originalCount);

            drawStack(enabled, render, 0, 0, 1F);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            if (hovered)  {
                gui.addTooltip(tooltip);
                gui.addTooltip(Strings.EMPTY);
                gui.addTooltip(String.format("Hold down %sSHIFT %sfor 5x", TextFormatting.AQUA, TextFormatting.RESET));
                gui.addTooltip(String.format("Hold down %sCTRL %sfor 10x", TextFormatting.GREEN, TextFormatting.RESET));
                gui.addTooltip(String.format("Hold down %sALT %sfor 25x", TextFormatting.LIGHT_PURPLE, TextFormatting.RESET));
            }

            GlStateManager.disableBlend();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (enabled) {
            PenguinNetwork.sendToServer(new PacketCraftBlueprint(blueprint, getCount(originalCount)));
        }
    }
}

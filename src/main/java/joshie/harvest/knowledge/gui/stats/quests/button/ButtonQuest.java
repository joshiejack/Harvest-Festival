package joshie.harvest.knowledge.gui.stats.quests.button;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.annotation.Nonnull;

public class ButtonQuest extends ButtonBook<GuiStats> {
    @Nonnull
    private final ItemStack stack;
    private final String description;


    public ButtonQuest(GuiStats gui, Quest quest, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, quest.getTitle());
        this.gui = gui;
        this.width = 120;
        this.height = 16;
        World world = MCClientHelper.getWorld();
        EntityPlayer player = MCClientHelper.getPlayer();
        this.stack = quest.getCurrentIcon(world, player);
        this.description = StringEscapeUtils.unescapeJava(quest.getDescription(world, player));
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            mouseDragged(mc, mouseX, mouseY);
            drawForeground();
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawString(TextFormatting.BOLD + displayString, x + 4, y - 8, 0x857754);
            mc.fontRenderer.drawSplitString(description, x + 20, y - 1, 100, 0x857754);
            mc.fontRenderer.setUnicodeFlag(flag);
            drawRect(x + 4, y + 17, x + 120, y + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private void drawForeground() {
        StackRenderHelper.drawStack(stack, x + 2, y + 1, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}

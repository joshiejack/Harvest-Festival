package joshie.harvest.knowledge.gui.stats.quests.button;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

public class ButtonQuest extends ButtonBook {
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
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            mouseDragged(mc, mouseX, mouseY);
            drawForeground();
            boolean flag = mc.fontRendererObj.getUnicodeFlag();
            mc.fontRendererObj.setUnicodeFlag(true);
            mc.fontRendererObj.drawString(TextFormatting.BOLD + displayString, xPosition + 4, yPosition - 8, 0x857754);
            mc.fontRendererObj.drawSplitString(description, xPosition + 20, yPosition - 1, 100, 0x857754);
            mc.fontRendererObj.setUnicodeFlag(flag);
            drawRect(xPosition + 4, yPosition + 17, xPosition + 120, yPosition + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private void drawForeground() {
        StackRenderHelper.drawStack(stack, xPosition + 2, yPosition + 1, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}

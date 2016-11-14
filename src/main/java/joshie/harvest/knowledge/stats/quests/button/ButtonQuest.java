package joshie.harvest.knowledge.stats.quests.button;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.RenderHelper;
import joshie.harvest.knowledge.stats.GuiStats;
import joshie.harvest.knowledge.stats.button.ButtonBook;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringEscapeUtils;

public class ButtonQuest extends ButtonBook {
    private final ItemStack stack;
    private final String description;

    public ButtonQuest(GuiStats gui, Quest quest, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, quest.getTitle());
        this.gui = gui;
        this.width = 120;
        this.height = 16;
        this.stack = HFNPCs.SPAWNER_NPC.getStackFromObject((NPC)quest.getCurrentNPC());
        this.description = StringEscapeUtils.unescapeJava(quest.getDescription());
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
            mc.fontRendererObj.drawSplitString(description, xPosition + 15, yPosition - 1, 105, 0x857754);
            mc.fontRendererObj.setUnicodeFlag(flag);
            drawRect(xPosition + 4, yPosition + 17, xPosition + 120, yPosition + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    private void drawForeground() {
        RenderHelper.drawStack(stack, xPosition, yPosition + 1, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}

package uk.joshiejack.settlements.client.gui.label;

import uk.joshiejack.settlements.client.gui.GuiNPC;
import uk.joshiejack.settlements.client.gui.NPCDisplayData;
import uk.joshiejack.settlements.client.renderer.item.NPCSpawnerRenderer;
import uk.joshiejack.settlements.npcs.status.Statuses;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class LabelRelationships extends LabelBook {
    private static final int MAX_RELATIONSHIP = 50000;
    private final ItemStack npcStack;
    private final NPCDisplayData npc;
    private final String name;
    private final int hearts;
    private final boolean hasMet;
    private final boolean hasTalkedTo;
    private final boolean hasGifted;

    public LabelRelationships(GuiBook gui, NPCDisplayData npc, int x, int y) {
        super(gui, x, y);
        this.width = 120;
        this.height = 16;
        this.npc = npc;
        this.name = npc.getLocalizedName();
        this.npcStack = npc.getIcon();
        this.hearts = (int)((((double) Statuses.getValue(npc.getRegistryName(), "relationship"))/ MAX_RELATIONSHIP) * 10);
        this.hasMet = Statuses.getValue(npc.getRegistryName(), "has_met") == 1;
        this.hasTalkedTo = Statuses.getValue(npc.getRegistryName(), "has_talked") == 1;
        this.hasGifted = Statuses.getValue(npc.getRegistryName(), "has_gifted") == 1;
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiNPC.ELEMENTS); //Elements
            drawRect(hasTalkedTo, 97, -9, 90, 220, 18);
            drawRect(hasGifted, 109, -9, 72, 220, 18);
            if (hasMet) drawStack(npcStack, 0, -4, 1.25F);
            else {
                NPCSpawnerRenderer.renderShadow = true;
                drawStack(npcStack, 0, -4, 1.25F); //Draw it with the shadow texture instead
                NPCSpawnerRenderer.renderShadow = false;
            }

            drawRect(x + 4, y + 17, x + 120, y + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            //Draw hearts
            if (!npc.getNPCClass().hideHearts()) {
                mc.getTextureManager().bindTexture(GuiElements.ICONS);
                for (int i = 0; i < 10; i++) {
                    drawTexturedModalRect(x + 15 + 10 * i, y + 6, 16, 0, 9, 9);
                    if (i < hearts) {
                        drawTexturedModalRect(x + 15 + +10 * i, y + 6, 52, 0, 9, 9);
                    }
                }
            }
        }

        GlStateManager.disableDepth();
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + (hasMet ? name : "???????"), x + 15, y - 3, 0x857754);
        mc.fontRenderer.setUnicodeFlag(flag);
        GlStateManager.enableDepth();
    }
}

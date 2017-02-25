package joshie.harvest.knowledge.gui.stats.relations.button;

import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.core.base.gui.ButtonBook;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import joshie.harvest.player.relationships.RelationshipDataClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class ButtonRelationsNPC extends ButtonBook<GuiStats> {
    private static final ResourceLocation HEARTS = new ResourceLocation("textures/gui/icons.png");
    private static final ItemStack GIFT = HFNPCs.TOOLS.getStackFromEnum(NPCTool.GIFT);
    private static final ItemStack TALK = HFNPCs.TOOLS.getStackFromEnum(NPCTool.SPEECH);
    private final int relationship;
    private final boolean gifted;
    private final boolean met;
    private final boolean talked;
    private final ItemStack stack;

    public ButtonRelationsNPC(GuiStats gui, NPC npc, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, npc.getLocalizedName());
        this.gui = gui;
        this.width = 120;
        this.height = 16;
        this.stack = HFNPCs.SPAWNER_NPC.getStackFromObject(npc);
        RelationshipDataClient data = HFTrackers.getClientPlayerTracker().getRelationships();
        this.relationship = data.getRelationship(npc);
        this.gifted = data.isStatusMet(npc, RelationStatus.GIFTED);
        this.talked = data.isStatusMet(npc, RelationStatus.TALKED);
        this.met = data.isStatusMet(npc, RelationStatus.MET);
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
            String text = met ? displayString : "???????";
            mc.fontRendererObj.drawString(TextFormatting.BOLD + text, xPosition + 15, yPosition - 3, 0x857754);
            mc.fontRendererObj.setUnicodeFlag(flag);
            drawRect(xPosition + 4, yPosition + 17, xPosition + 120, yPosition + 18, 0xFF857754);
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            //Draw hearts
            int hearts = (int)((((double)relationship)/ RelationshipType.NPC.getMaximumRP()) * 10);
            mc.getTextureManager().bindTexture(HEARTS);
            for (int i = 0; i < 10; i++) {
                drawTexturedModalRect(xPosition + 15 + 10 * i, yPosition + 6, 16, 0, 9, 9);
                if (i < hearts) {
                    drawTexturedModalRect(xPosition + 15 + 10 * i, yPosition + 6, 52, 0, 9, 9);
                }
            }
        }
    }

    private void drawForeground() {
        if (!talked) {
            StackRenderHelper.drawGreyStack(TALK, xPosition + 100, yPosition - 2, 0.5F);
        } else StackRenderHelper.drawStack(TALK, xPosition + 100, yPosition - 2, 0.5F);

        if (!gifted) {
            StackRenderHelper.drawGreyStack(GIFT, xPosition + 110, yPosition - 2, 0.5F);
        } else StackRenderHelper.drawStack(GIFT, xPosition + 110, yPosition - 2, 0.5F);

        if (!met) {
            StackRenderHelper.drawGreyStack(stack, xPosition, yPosition, 1F);
        } else StackRenderHelper.drawStack(stack, xPosition, yPosition, 1F);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }
}

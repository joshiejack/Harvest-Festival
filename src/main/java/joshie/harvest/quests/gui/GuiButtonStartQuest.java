package joshie.harvest.quests.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.quests.packet.PacketQuestStart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class GuiButtonStartQuest extends GuiButton {
    private final BlockPos pos;
    private final Quest quest;

    public GuiButtonStartQuest(BlockPos pos, Quest quest, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.pos = pos;
        this.quest = quest;
        width = 70;
        height = 30;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, (state * 70) - 70, 226, width, height);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PacketHandler.sendToServer(new PacketQuestStart(pos, quest));
    }
}

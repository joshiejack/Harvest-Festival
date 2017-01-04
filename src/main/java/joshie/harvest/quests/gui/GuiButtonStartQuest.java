package joshie.harvest.quests.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.quests.packet.PacketQuestStart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class GuiButtonStartQuest extends GuiButton {
    private BlockPos pos;
    private Quest quest;

    public GuiButtonStartQuest(BlockPos pos, Quest quest, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.pos = pos;
        this.quest = quest;
        width = 70;
        height = 30;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            //mc.getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPosition, yPosition, (state * 70) - 70, 226, width, height);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PacketHandler.sendToServer(new PacketQuestStart(pos, quest, quest.writeToNBT(new NBTTagCompound())));
    }
}

package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.ChatFontRenderer;
import joshie.harvest.core.util.GuiBaseContainer;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.packet.PacketClose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public abstract class GuiNPCBase extends GuiBaseContainer {
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODID, "textures/gui/chatbox.png");
    protected final EntityNPC npc;
    protected final EntityPlayer player;
    protected final int nextGui;
    private final int inside;
    private final int outside;

    public GuiNPCBase(EntityPlayer ePlayer, EntityNPC eNpc, EnumHand hand, int next) {
        super(new ContainerNPCChat(ePlayer, eNpc, hand, next), "chat", 0);

        hasInventory = false;
        npc = eNpc;
        player = ePlayer;
        xSize = 256;
        ySize = 256;
        nextGui = next;
        inside = npc.getNPC().getInsideColor();
        outside = npc.getNPC().getOutsideColor();
    }

    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.pushMatrix();
        mc.renderEngine.bindTexture(chatbox);
        drawTexturedModalRect(x, y + 150, 0, 150, 256, 51);
        GlStateManager.enableBlend();
        ChatFontRenderer.colorise(inside);
        drawTexturedModalRect(x, y + 150, 0, 100, 256, 51);
        ChatFontRenderer.colorise(outside);
        drawTexturedModalRect(x, y + 150, 0, 50, 256, 51);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        ChatFontRenderer.render(this, x, y, npc.getName(), inside, outside);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    private void drawHeart(int value) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        int xPos = (int) ((((double) value / (HFNPCs.MAX_FRIENDSHIP))) * 7);
        drawTexturedModalRect(240, 130, 0, 0, 25, 25);
        drawTexturedModalRect(240, 130, 25 + (25 * xPos), 0, 25, 25);
    }

    @Override
    public void drawForeground(int x, int y) {
        boolean originalFlag = fontRendererObj.getUnicodeFlag();
        fontRendererObj.setUnicodeFlag(true);
        mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
        if (npc.getNPC().isMarriageCandidate()) {
            drawHeart(HFApi.relationships.getRelationship(player, npc.getNPC().getUUID()));
        }

        drawOverlay(x, y);
        fontRendererObj.setUnicodeFlag(originalFlag);
    }

    public abstract void drawOverlay(int x, int y);

    @Override
    public void drawDefaultBackground() {}

    public String getScript() {
        return "missing chat";
    }

    public void endChat() {
        PacketHandler.sendToServer(new PacketClose());
    }
}
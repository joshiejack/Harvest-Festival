package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.ChatFontRenderer;
import joshie.harvest.core.util.GuiBaseContainer;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiNPCBase extends GuiBaseContainer {
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODID, "textures/gui/chatbox.png");
    protected EntityNPC npc;
    protected EntityPlayer player;
    protected int nextGui;
    private int inside;
    private int outside;

    public GuiNPCBase(EntityNPC eNpc, EntityPlayer ePlayer, int next) {
        super(new ContainerNPCBase(eNpc, ePlayer.inventory), "chat", 0);

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
        GlStateManager.enableBlend();
        mc.renderEngine.bindTexture(chatbox);
        drawTexturedModalRect(x, y + 150, 0, 150, 256, 51);
        ChatFontRenderer.colorise(inside);
        drawTexturedModalRect(x, y + 150, 0, 100, 256, 51);
        ChatFontRenderer.colorise(outside);
        drawTexturedModalRect(x, y + 150, 0, 50, 256, 51);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        ChatFontRenderer.render(this, x, y, npc.getName(), npc.getNPC().getInsideColor(), npc.getNPC().getOutsideColor());
    }

    private void drawHeart(int value) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        int xPos = (int) ((((double) value / (HFNPCs.MAX_FRIENDSHIP))) * 7);
        drawTexturedModalRect(240, 130, 0, 0, 25, 25);
        drawTexturedModalRect(240, 130, 25 + (25 * xPos), 0, 25, 25);
    }

    @Override
    public void drawForeground(int x, int y) {
        mc.renderEngine.bindTexture(HFModInfo.elements);
        if (npc.getNPC().isMarriageCandidate()) {
            drawHeart(HFApi.relationships.getRelationship(player, npc.getRelatable()));
        }
    }

    public String getScript() {
        return "missing chat";
    }

    public void endChat() {
        player.closeScreen();
        if (nextGui != -1) {
            player.openGui(HarvestFestival.instance, nextGui, player.worldObj, npc.getEntityId(), 0, -1);
        }
    }
}
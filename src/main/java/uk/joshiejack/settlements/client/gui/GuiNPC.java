package uk.joshiejack.settlements.client.gui;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.client.gui.button.ButtonScriptCaller;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.network.npc.PacketEndChat;
import uk.joshiejack.penguinlib.client.gui.GuiPenguin;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static uk.joshiejack.settlements.Settlements.MODID;

@SideOnly(Side.CLIENT)
public abstract class GuiNPC extends GuiPenguin {
    private static final ResourceLocation BACKGROUND = GuiElements.getTexture(MODID, "chat");
    public static final ResourceLocation CHATBOX = GuiElements.getTexture(MODID, "chatbox");
    public static final ResourceLocation ELEMENTS = GuiElements.getTexture(MODID, "npc_elements");
    protected static final List<NPCButtons.ButtonData> BUTTONS = Lists.newArrayList();
    protected final EntityNPC npc;
    protected final int inside;
    protected final int outside;

    public GuiNPC(EntityNPC npc) {
        super(BACKGROUND);
        this.xSize = 256;
        this.ySize = 256;
        this.npc = npc;
        short factor = 200;// 0-255;
        super.inside = (factor << 24) | (npc.getInfo().getInsideColor() & 0x00ffffff);
        super.outside = (220 << 24) | (npc.getInfo().getOutsideColor() & 0x00ffffff);
        this.inside = npc.getInfo().getInsideColor();
        this.outside = npc.getInfo().getOutsideColor();
    }

    public static void setButtons(List<NPCButtons.ButtonData> buttons) {
        BUTTONS.clear();
        BUTTONS.addAll(buttons);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        PenguinNetwork.sendToServer(new PacketEndChat(npc.getEntityId())); //Remove the player
    }

    @Override
    public void initGui() {
        super.initGui();
        for (int i = 0; i < BUTTONS.size(); i++) {
            NPCButtons.ButtonData b = BUTTONS.get(i);
            if (i < 2) {
                buttonList.add(new ButtonScriptCaller(this, b, inside, outside, npc.getEntityId(), buttonList.size(), guiLeft + 241, sr.getScaledHeight() - 96 + (i * 21)));
            } else
                buttonList.add(new ButtonScriptCaller(this, b, inside, outside, npc.getEntityId(), buttonList.size(), guiLeft - 3, sr.getScaledHeight() - 96 + ((i - 2) * 21)).invert());
        }
    }

    @Override
    public void drawDefaultBackground() {
    }


    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.pushMatrix();
        mc.renderEngine.bindTexture(CHATBOX);
        drawTexturedModalRect(x, sr.getScaledHeight() - 101, 0, 150, 256, 51);
        GlStateManager.enableBlend();
        ChatFontRenderer.colorise(inside);
        drawTexturedModalRect(x, sr.getScaledHeight() - 101, 0, 100, 256, 51);
        ChatFontRenderer.colorise(outside);
        drawTexturedModalRect(x, sr.getScaledHeight() - 101, 0, 50, 256, 51);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        ChatFontRenderer.render(this, x, sr.getScaledHeight() - 101 - 150, npc.getName(), inside, outside);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    @Override
    public void drawForeground(int x, int y) {
        boolean originalFlag = fontRenderer.getUnicodeFlag();
        fontRenderer.setUnicodeFlag(true);
        mc.renderEngine.bindTexture(ELEMENTS);
        GlStateManager.color(1F, 1F, 1F);
        RenderHelper.enableGUIStandardItemLighting();
        drawOverlay(x, y);
        fontRenderer.setUnicodeFlag(originalFlag);
    }

    protected abstract void drawOverlay(int x, int y);
}

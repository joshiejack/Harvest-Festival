package uk.joshiejack.settlements.client.gui.button;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.client.gui.ChatFontRenderer;
import uk.joshiejack.settlements.client.gui.GuiNPC;
import uk.joshiejack.settlements.client.gui.NPCButtons;
import uk.joshiejack.settlements.network.npc.PacketButtonPressed;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.annotation.Nonnull;

import static uk.joshiejack.settlements.client.gui.GuiNPC.CHATBOX;

public class ButtonScriptCaller extends GuiButton {
    private final GuiNPC gui;
    private final ResourceLocation quest;
    private final ItemStack stack;
    private final String name;
    private final int entityID;
    private final int inside;
    private final int outside;
    private boolean invert;

    public ButtonScriptCaller(GuiNPC gui, NPCButtons.ButtonData data, int inside, int outside, int entityID, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.gui = gui;
        this.width = 19;
        this.height = 20;
        this.quest = data.getScript();
        this.stack = data.getIcon();
        this.name = data.getName();
        this.inside = inside;
        this.outside = outside;
        this.entityID = entityID;
    }

    public ButtonScriptCaller invert() {
        this.invert = true;
        return this;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        StackRenderHelper.drawStack(stack, x + (invert ? 2 : 1), y + 2, 1F);
        hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        mc.getTextureManager().bindTexture(CHATBOX);
        if (!hovered) {
            ChatFontRenderer.colorise(inside);
        } else gui.addTooltip(Lists.newArrayList(StringHelper.localize(StringEscapeUtils.unescapeJava(name)).split("\n")));

        drawTexturedModalRect(x, y, 218, invert ? 20 : 0, 19, 20); //Inside
        ChatFontRenderer.colorise(outside);
        drawTexturedModalRect(x - (invert ? 1 : 0), y, 237, invert ? 20 : 0, 19, 20); //Outside
        GlStateManager.color(1F, 1F, 1F);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        Minecraft.getMinecraft().player.closeScreen();
        PenguinNetwork.sendToServer(new PacketButtonPressed(quest, entityID));
    }
}

package uk.joshiejack.settlements.client.gui;

import joptsimple.internal.Strings;
import uk.joshiejack.settlements.client.gui.button.ButtonAnswer;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.network.npc.PacketAnswer;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiNPCAsk extends GuiNPC {
    private final ResourceLocation quest;
    private final boolean isQuest;
    private final String question;
    private final String[] answers;
    public int selected;
    private final boolean hasQuestion;
    private int startY;
    private boolean finished;
    private final int chatboxHeight;

    public GuiNPCAsk(EntityNPC npc, ResourceLocation quest, boolean isQuest, String question, String[] answers, String... formatting) {
        super(npc);
        this.quest = quest;
        this.isQuest = isQuest;
        this.question = Chatter.modify(modify(quest, isQuest, question), formatting);
        this.hasQuestion = !Strings.isNullOrEmpty(question);
        this.answers = new String[answers.length];
        for (int i = 0; i < answers.length; i++) {
            this.answers[i] = Chatter.modify(modify(quest, isQuest, answers[i]), formatting);
        }

        chatboxHeight = Math.max(0, (answers.length + (hasQuestion ? 1 : 0)) - 5);
    }

    public static String modify(ResourceLocation quest, boolean isQuest, String text) {
        Interpreter it = isQuest ? Quest.REGISTRY.get(quest).getInterpreter() : Scripting.get(quest);
        String key = it.getLocalizedKey(quest, text);
        String localized = StringHelper.localize(key);
        if (!localized.equals(key)) return localized; //Translation key instead, if it exists
        else return text;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.startY = hasQuestion ? (sr.getScaledHeight() / 2) + 40 : (sr.getScaledHeight() / 2) + 32;
        for (int i = 0; i < answers.length; i++) {
            buttonList.add(new ButtonAnswer(this, i, buttonList.size(), guiLeft + 20, guiTop - 1 + startY + (i * 9)));
        }
    }

    private void adjustSelection(int amount) {
        int newSelection = selected + amount;
        if (newSelection >= answers.length) {
            newSelection = answers.length - 1;
        } else if (newSelection < 0) {
            newSelection = 0;
        }

        selected = newSelection;
    }

    public void setFinished() {
        this.finished = true;
        mc.player.closeScreen();
    }

    //Scale the background
    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.pushMatrix();
        //BLACK BOX
        mc.renderEngine.bindTexture(CHATBOX);
        //TOP
        drawTexturedModalRect(x, sr.getScaledHeight() - 101, 0, 150, 256, 25); //51 total  TOP =
        //BOTTOM
        drawTexturedModalRect(x, sr.getScaledHeight() - 76 + (chatboxHeight * 9), 0, 173, 256, 26);
        //GAP FILLER
        for (int i = 0; i < chatboxHeight; i++) {
            drawTexturedModalRect(x, sr.getScaledHeight() - 76 + (i * 9), 0, 167, 256, 9);
        }

        GlStateManager.enableBlend();
        ChatFontRenderer.colorise(inside);
        //INSIDE COLOURS
        //TOP
        drawTexturedModalRect(x, sr.getScaledHeight() - 100, 0, 100, 256, 25); //51 total  TOP =
        //BOTTOM
        drawTexturedModalRect(x, sr.getScaledHeight() - 75 + (chatboxHeight * 9), 0, 125, 256, 26);
        //GAP FILLER
        for (int i = 0; i < chatboxHeight; i++) {
            drawTexturedModalRect(x, sr.getScaledHeight() - 75 + (i * 9), 0, 127, 256, 9);
        }

        ChatFontRenderer.colorise(outside);
        //OUTSIDE COLOURS
        //TOP
        drawTexturedModalRect(x, sr.getScaledHeight() - 100, 0, 50, 256, 25); //51 total  TOP =
        //BOTTOM
        drawTexturedModalRect(x, sr.getScaledHeight() - 75 + (chatboxHeight * 9), 0, 75, 256, 26);
        //GAP FILLER
        for (int i = 0; i < chatboxHeight; i++) {
            drawTexturedModalRect(x, sr.getScaledHeight() - 75 + (i * 9), 0, 78, 256, 9);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        ChatFontRenderer.render(this, x, sr.getScaledHeight() - 101 - 150, npc.getName(), inside, outside);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    @Override
    public void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);

        //W or Up
        if (character == 'w' || key == 200) {
            adjustSelection(-1);
        }

        //S or Down
        if (character == 's' || key == 208) {
            adjustSelection(+1);
        }

        //Enter or Spacebar or Q
        if (key == 28 || key == 57 || character == 'q') {
            setFinished(); //Selection has been marked
        }
    }

    @Override
    public void onGuiClosed() {
        if (finished) {
            this.finished = false; //Reset the value
            PenguinNetwork.sendToServer(new PacketAnswer(npc.getEntityId(), quest, isQuest, selected)); //Remove the player
        } else super.onGuiClosed();
    }

    @Override
    protected void drawOverlay(int x, int y) {
        if (hasQuestion) fontRenderer.drawString(TextFormatting.BOLD + question, 20, (sr.getScaledHeight() / 2) + 32, 0xFFFFFF);
        for (int i = 0; i < answers.length; i++) {
            fontRenderer.drawString(TextFormatting.BOLD + answers[i], 40, startY + (i * 9), 0xFFFFFF);
        }

        GlStateManager.color(1F, 1F, 1F);
        mc.renderEngine.bindTexture(ELEMENTS);
        drawTexturedModalRect(20, startY + 1 + (selected * 9), 0, 32, 19, 8);
    }
}

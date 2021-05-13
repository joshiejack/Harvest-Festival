package uk.joshiejack.settlements.client.gui;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.gui.button.ButtonQuestStart;
import uk.joshiejack.settlements.quest.settings.QuestBoardClient;
import uk.joshiejack.penguinlib.client.gui.GuiPenguin;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import java.util.Map.Entry;


public class GuiQuestBoard  extends GuiPenguin {
    public static final ResourceLocation TEXTURE = GuiElements.getTexture(Settlements.MODID, "quest_board");
    private final ResourceLocation quest;
    private final String text;

    public GuiQuestBoard() {
        super(TEXTURE);
        xSize = 226;
        ySize = 200;
        Entry<ResourceLocation, QuestBoardClient.QuestInfo> info = QuestBoardClient.getNextQuest();
        if (info != null) {
            quest = info.getKey();
            text = info.getValue().getDescription();
        } else {
            quest = null; //BAAAAAAAAAd
            text = null; ///EEEEEEEEEEEEWWWWWWWW
            //Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        if (quest != null) {
            buttonList.add(new ButtonQuestStart(quest, guiLeft + 78, guiTop + 152));
        }
    }

    @Override
    public void drawForeground(int x, int y) {
        if (text != null) {
            fontRenderer.drawSplitString(StringHelper.localize(text), 42, 38, 140, 4210752);
        } else fontRenderer.drawString(StringHelper.localize("no active quests were found"), 42, 38, 4210752);
    }
}

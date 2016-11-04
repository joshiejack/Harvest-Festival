package joshie.harvest.npc.gui;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.packet.PacketGift;
import joshie.harvest.npc.packet.PacketInfo;
import joshie.harvest.quests.QuestHelper;
import joshie.harvest.quests.packet.PacketQuestSelect;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.Arrays;

/** Renders a selection menu gui **/
public class GuiNPCSelect extends GuiNPCBase {
    private Selection selection;
    private Quest quest;
    private String[] text;
    private int optionsStart;
    private int optionsTotal;
    private int selected;

    public GuiNPCSelect(EntityPlayer player, EntityNPC npc, int next, int selectionType) {
        super(player, npc, EnumHand.MAIN_HAND, next);
        if (selectionType == -1) selection = npc.getNPC().getShop().getSelection();
        else {
            quest = QuestHelper.getSelectiomFromID(player, selectionType);
            selection = quest != null ? quest.getSelection(player, npc.getNPC()): null;
        }

        if (selection == null || selection.getText() == null);//player.closeScreen();
        else {
            optionsTotal = 0;
            text = Arrays.copyOf(selection.getText(), selection.getText().length);
            selected = 1;
            boolean optionStarted = false;
            for (int i = 0; i < text.length; i++) {
                String original = TextHelper.localize(text[i]);
                String replaced = replace(original);
                if (!original.equals(replaced)) {
                    if (!optionStarted) {
                        optionStarted = true;
                        optionsStart = (byte) i;
                    }

                    optionsTotal++;
                }

                text[i] = replaced;
            }
        }
    }

    private String replace(String string) {
        return string.replace("@", "    ");
    }

    @Override
    public void drawOverlay(int x, int y) {
        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                fontRendererObj.drawString(TextFormatting.BOLD + text[i], 22, 156 + (i * 10), 0xFFFFFF);
            }

            GlStateManager.color(1F, 1F, 1F);
            mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);

            int position = selected + optionsStart;
            if (position == 1) {
                drawTexturedModalRect(20, 157, 0, 32, 19, 8);
            } else if (position == 2) {
                drawTexturedModalRect(20, 167, 0, 32, 19, 8);
            } else if (position == 3) {
                drawTexturedModalRect(20, 177, 0, 32, 19, 8);
            } else if (position == 4) {
                drawTexturedModalRect(20, 187, 0, 32, 19, 8);
            }
        }
    }

    public void adjustSelection(int number) {
        int newSelection = selected + number;
        if (newSelection > optionsTotal) {
            newSelection = 1;
        } else if (newSelection < 1) {
            newSelection = optionsTotal;
        }

        selected = (byte) newSelection;
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
            select();
        }
    }

    private boolean isValidOption(int option) {
        int min = optionsStart; //1
        int max = optionsStart + optionsTotal; //2
        return option >= min && option <= max;
    }


    @Override
    public void onMouseClick(int mouseX, int mouseY) {
        if (isPointInRegion(242, 156, 17, 19, npcMouseX, npcMouseY))
            PacketHandler.sendToServer(new PacketGift(npc));
        else if (npc.getNPC().hasInfo() != null && isPointInRegion(242, 177, 17, 19, npcMouseX, npcMouseY))
            PacketHandler.sendToServer(new PacketInfo(npc));
        else if (selection != null) {
            if (mouseY >= 156 && mouseY <= 164 && isValidOption(0)) {
                selected = 1;
                select();
            } else if (mouseY >= 166 && mouseY <= 174 && isValidOption(1)) {
                selected = optionsStart == 0 ? 2 : 1;
                select();
            } else if (mouseY >= 176 && mouseY <= 184 && isValidOption(2)) {
                selected = (optionsStart == 0) ? 3 : (optionsStart == 1) ? 2 : 1;
                select();
            } else if (mouseY >= 186 && mouseY <= 196 && isValidOption(3)) {
                selected = (optionsStart == 0) ? 4 : (optionsStart == 1) ? 3 : (optionsStart == 2) ? 2 : 1;
                select();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void select() {
        if (quest != null && quest.getQuestType() == QuestType.TOWN) {
            TownData town = TownHelper.getClosestTownToEntity(npc);
            PacketHandler.sendToServer(new PacketQuestSelect(quest, npc, selected).setUUID(town.getID()));
        } else PacketHandler.sendToServer(new PacketQuestSelect(quest, npc, selected));
    }

}
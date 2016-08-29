package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.Selection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.io.IOException;
import java.util.Arrays;

/** Renders a selection menu gui **/
public class GuiNPCSelect extends GuiNPCBase {
    private static final Selection SHOPS = new ShopSelection();
    private Selection selection = new ShopSelection();
    private Quest quest;
    private String[] text;
    private int optionsStart;
    private int optionsTotal;
    private int selected;

    public GuiNPCSelect(EntityNPC npc, EntityPlayer player, int next, int selectionType) {
        super(npc, player, next);
        if (selectionType == -1) selection = SHOPS;
        else {
            quest = HFTrackers.getClientPlayerTracker().getQuests().getAQuest(Quest.REGISTRY.getValues().get(selectionType));
            selection = quest.getSelection(npc.getNPC());
        }

        if (selection == null || selection.getText() == null) player.closeScreen();
        else {
            optionsTotal = 0;
            text = Arrays.copyOf(selection.getText(), selection.getText().length);
            selected = 1;
            boolean optionStarted = false;
            for (int i = 0; i < text.length; i++) {
                String original = Text.localize(text[i]);
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
        return string.replace("@", "     ");
    }

    @Override
    public void drawForeground(int x, int y) {
        super.drawForeground(x, y);
        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                fontRendererObj.drawStringWithShadow(text[i], 22, 158 + (i * 10), 0xFFFFFF);
            }

            mc.renderEngine.bindTexture(HFModInfo.elements);

            int position = selected + optionsStart;
            if (position == 1) {
                drawTexturedModalRect(20, 158, 0, 32, 19, 8);
            } else if (position == 2) {
                drawTexturedModalRect(20, 168, 0, 32, 19, 8);
            } else if (position == 3) {
                drawTexturedModalRect(20, 178, 0, 32, 19, 8);
            } else if (position == 4) {
                drawTexturedModalRect(20, 188, 0, 32, 19, 8);
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
        if (selection != null) {
            if (mouseY >= 158 && mouseY <= 166 && isValidOption(0)) {
                selected = 1;
                select();
            } else if (mouseY >= 168 && mouseY <= 176 && isValidOption(1)) {
                selected = optionsStart == 0 ? 2 : 1;
                select();
            } else if (mouseY >= 178 && mouseY <= 186 && isValidOption(2)) {
                selected = (optionsStart == 0) ? 3 : (optionsStart == 1) ? 2 : 1;
                select();
            } else if (mouseY >= 188 && mouseY <= 196 && isValidOption(3)) {
                selected = (optionsStart == 0) ? 4 : (optionsStart == 1) ? 3 : (optionsStart == 2) ? 2 : 1;
                select();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void select() {
        Result result = selection.onSelected(player, npc, npc.getNPC(), quest, selected);
        if (result == Result.ALLOW) player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), -1, -1);
        else if (result == Result.DENY) player.closeScreen();
    }

    private static class ShopSelection extends Selection {
        ShopSelection() {
            super("harvestfestival.shop.general.options", "harvestfestival.shop.general.options.shop", "harvestfestival.shop.general.options.chat");
        }

        /** Called when the option is selected **/
        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, Quest quest, int option) {
            if (option == 1) {
                if (npc.isBuilder()) player.openGui(HarvestFestival.instance, GuiHandler.SHOP_BUILDER, player.worldObj, entity.getEntityId(), 0, 0);
                else player.openGui(HarvestFestival.instance, GuiHandler.SHOP_MENU, player.worldObj, entity.getEntityId(), 0, 0);
                return Result.DEFAULT;
            }

            return Result.ALLOW;
        }
    }
}
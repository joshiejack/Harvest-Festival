package joshie.harvest.npc.gui;

import java.util.Arrays;

import joshie.harvest.HarvestFestival;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

/** Renders a selection menu gui **/
public class GuiNPCSelect extends GuiNPCBase {
    private static final ShopSelection selection = new ShopSelection();
    public String[] text;
    public byte optionsStart;
    public byte optionsTotal;
    public byte selected;

    public GuiNPCSelect(EntityNPC npc, EntityPlayer player) {
        super(npc, player);
        optionsTotal = 0;
        text = Arrays.copyOf(selection.getText(), selection.getText().length);
        selected = 1;
        boolean optionStarted = false;
        for (int i = 0; i < text.length; i++) {
            String original = text[i];
            String replaced = replace(text[i]);
            if (!original.equals(replaced)) {
                if (!optionStarted) {
                    optionStarted = true;
                    optionsStart = (byte) i;
                }

                text[i] = replaced;
                optionsTotal++;
            }
        }
    }

    private String replace(String string) {
        return string.replace("@", "     ");
    }

    @Override
    public void drawForeground(int x, int y) {
        super.drawForeground(x, y);
        for (int i = 0; i < text.length; i++) {
            fontRendererObj.drawStringWithShadow(text[i], 22, 158 + (i * 10), 0xFFFFFF);
        }

        mc.renderEngine.bindTexture(HFModInfo.elements);

        int position = selected + optionsStart;
        if (position == 1) {
            drawTexturedModalRect(20, 159, 0, 32, 19, 8);
        } else if (position == 2) {
            drawTexturedModalRect(20, 169, 0, 32, 19, 8);
        } else if (position == 3) {
            drawTexturedModalRect(20, 179, 0, 32, 19, 8);
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
    public void keyTyped(char character, int key) {
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
            selection.onSelected(npc, player, selected);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY) {
        if (mouseY >= 158 && mouseY <= 166 && optionsTotal == 3) {
            if (selected == 1) selection.onSelected(npc, player, selected);
            else selected = 1;
        } else if (mouseY >= 168 && mouseY <= 176 && optionsTotal >= 2) {
            int check = optionsTotal == 3 ? 2 : 1;
            if (selected == check) selection.onSelected(npc, player, selected);
            else selected = (byte) check;
        } else if (mouseY >= 178 && mouseY <= 186 && optionsTotal >= 1) {
            int check = optionsTotal == 3 ? 3 : optionsTotal == 2 ? 2 : 1;
            if (selected == check) selection.onSelected(npc, player, selected);
            else selected = (byte) check;
        } else selection.onSelected(npc, player, selected);
    }

    public static class ShopSelection {
        private String[] lines = new String[3];

        public ShopSelection() {
            lines[0] = "What do you want to do?";
            lines[1] = "@Shop";
            lines[2] = "@Chat";
        }

        /** Return the text **/
        public String[] getText() {
            return lines;
        }

        /** Return a value between 2-3 **/
        public int getMaxOptions() {
            return 2;
        }

        /** Called when the option is selected **/
        public void onSelected(EntityNPC npc, EntityPlayer player, int option) {
            if (option == 1) {
                player.openGui(HarvestFestival.instance, GuiHandler.SHOP_MENU, player.worldObj, npc.getEntityId(), 0, 0);
            } else if (option == 2) {
                player.openGui(HarvestFestival.instance, GuiHandler.NPC, player.worldObj, npc.getEntityId(), 0, 0);
            }
        }
    }
}

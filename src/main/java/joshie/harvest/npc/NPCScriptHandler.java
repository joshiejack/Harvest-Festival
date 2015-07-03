package joshie.harvest.npc;

import java.util.Arrays;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.gui.GuiNPC;
import joshie.harvest.player.stats.StatData;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

public class NPCScriptHandler {
    private static final int MAX_LINES_PER_PAGE = 3;
    protected String[][] script; //This is an array of [page][line], with line ALWAYS beign a length of MAX_LINES ^
    protected int page; //Current page displayed
    protected int line; //Current lines displayed
    protected double character; //A ticker, Determines what character we should be displaying
    protected boolean finished; //Whether the text has finished displaying
    protected boolean hasConfirmation; //Whether this page of the script has a cofnrimation screen
    protected boolean selectedBottom; //True if Yes is Selected, False if no is Selected
    protected boolean executed; //Whether the whole thing was executed
    protected String text;

    public void confirm() {} //Called when the player clicks the top answer in a selection menu
    public void cancel() {} //Called when they player clicks the bottom answer in a selection menu

    // Return the next Script for this npc to display
    public NPCScriptHandler next(EntityPlayer player, INPC npc) {
        return null;
    }

    private EntityPlayer player;
    private EntityNPC npc;
    private GuiNPC gui;

    public NPCScriptHandler start(GuiNPC gui, EntityPlayer player, EntityNPC npc) {
        this.gui = gui;
        this.player = player;
        this.npc = npc;
        executed = false;
        finished = false;
        character = 0;
        page = 0;
        line = 0;
        String[] original = WordUtils.wrap(format(gui.getScript()), 39).split(SystemUtils.LINE_SEPARATOR);
        if (original != null) {
            int size = original.length / MAX_LINES_PER_PAGE;
            boolean isRemainder = original.length % MAX_LINES_PER_PAGE == 0;
            if (!isRemainder) {
                size++;
            }

            int start = 0;
            script = new String[size][MAX_LINES_PER_PAGE];
            for (int i = 0; i < size; i++) {
                int length = ((start + MAX_LINES_PER_PAGE) > original.length) ? original.length : (start + MAX_LINES_PER_PAGE);
                String[] subtext = Arrays.copyOfRange(original, start, length);
                for (int j = 0; j < subtext.length; j++) {
                    script[i][j] = subtext[j];
                }

                start = start + MAX_LINES_PER_PAGE;
            }
        }
        return this;
    }

    private String format(String string) {
        if (string == null) return "FORGOT SOME TEXT DUMBASS";
        StatData stats = HFTrackers.getClientPlayerTracker().getStats();
        string = string.replace("<BR>", SystemUtils.LINE_SEPARATOR);
        string = string.replace("Þ", player.getDisplayName());
        string = string.replace("ℇ", npc.getNPC().getUnlocalizedName());
        string = string.replace("$", "" + stats.getGold());

        if (npc.getLover() != null) {
            string = string.replace("❤", npc.getLover().getNPC().getUnlocalizedName());
        } else string = string.replace("❤", Translate.translate("nolover"));

        return string.replace("♥", HFTrackers.getClientPlayerTracker().getRelationships().getLover());
    }

    public void draw(GuiScreen gui, FontRenderer font) {
        if (hasConfirmation) {
            if (!selectedBottom) {
                gui.drawTexturedModalRect(20, 169, 0, 32, 19, 8);
            } else {
                gui.drawTexturedModalRect(20, 179, 0, 32, 19, 8);
            }
        }

        //Set the text to finished if we've reached the last line
        if (!finished) {
            if (line >= 2) {
                finished = true;
            }
        }

        //Draws all the current 'completed' strings to the gui
        for (int i = 0; i < line; i++) {
            String text = script[page][i];
            if (text != null) {
                if (text.startsWith("@")) {
                    text = text.replace("@", "     ");
                    hasConfirmation = true;
                }

                font.drawStringWithShadow(text, 22, 158 + (i * 10), 0xFFFFFF);
            }
        }

        //If the page we are trying to parse, has a string for the line we're trying to display
        if (line < MAX_LINES_PER_PAGE) {//If the current line, is less than the length of the lines, And we have less pages than max
            if (script[page][line] != null) {
                //Convert the next line in to a char array
                char[] todisplay = script[page][line].toCharArray();
                if (todisplay.length > 0) {
                    if (new String("" + todisplay[0]).equals("@")) {
                        character = todisplay.length;
                    }

                    if (character < todisplay.length) { //If the current position of the char array, is less than it's maximum
                        character += 0.2D; //Increase the tick, slowly
                    }
                }

                //Create a new set of chars, this is what we will display
                char[] fordisplay = new char[(int) Math.ceil(character)];
                for (int i = 0; i < fordisplay.length; i++) {
                    if (i < todisplay.length) {
                        fordisplay[i] = todisplay[i]; //Copy all the characters over to the new array
                    }
                }

                //Draw the characters as we go.
                font.drawStringWithShadow(new String(fordisplay), 22, 158 + (line * 10), 0xFFFFFF);

                //Now if we have completed the entire array, let's reset the position and increase the line
                if (fordisplay.length >= todisplay.length) {
                    character = 0;
                    line++;
                }
            } else finished = true;
        }
    }

    public void keyTyped(char character, int key) {
        if (character == 'w' || key == 200) {
            selectedBottom = false;
        }

        if (character == 's' || key == 208) {
            selectedBottom = true;
        }

        if (key == 28 || key == 57 || character == 'q') {
            select();
        }
    }

    private void select() {
        if (!selectedBottom) {
            confirm();
        } else cancel();

        /*HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
        for (IQuest quest : quests) {
            if (quest != null) {
                if (!selectedBottom) {
                    quest.confirm(player, npc);
                } else quest.cancel(player, npc);

                hasConfirmation = false;
                nextChat();
            }
        } */
    }

    protected void nextChat() {
        if (!finished) {
            finished = true;
            line = 3;
        } else if (finished && page < (script.length - 1)) {
            finished = false; //Reset the page being finished
            line = 0; //Reset the line we are currently reading
            page++; //Reset the page we are currently reading
        } else {
            executed = true;
            gui.endChat();
        }
    }

    public boolean executed() {
        return executed;
    }

    public void onMouseClick(int mouseX, int mouseY) {
        //Confirm
        if (hasConfirmation) {
            if (mouseY >= 168 && mouseY <= 176) {
                selectedBottom = false;
                select();
            }

            //Cancel
            if (mouseY >= 178 && mouseY <= 186) {
                selectedBottom = true;
                select();
            }
        } else nextChat();
    }
}

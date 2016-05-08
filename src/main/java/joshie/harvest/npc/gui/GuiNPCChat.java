package joshie.harvest.npc.gui;

import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.player.stats.StatData;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.Arrays;

/** Renders a chat script **/
public class GuiNPCChat extends GuiNPCBase {
    private static final int MAX_LINES_PER_PAGE = 3;
    protected String[][] script; //This is an array of [page][line], with line ALWAYS beign a length of MAX_LINES ^
    protected int page; //Current page displayed
    protected int line; //Current lines displayed
    protected double character; //A ticker, Determines what character we should be displaying
    protected boolean finished; //Whether the text has finished displaying
    protected boolean executed; //Whether the whole thing was executed
    private boolean isScriptInit = false;
    private int nextGui = -1;

    private String format(String string) {
        if (string == null) return "FORGOT SOME TEXT DUMBASS";
        StatData stats = HFTrackers.getClientPlayerTracker().getStats();
        string = string.replace("<BR>", SystemUtils.LINE_SEPARATOR);
        string = string.replace("%p", player.getDisplayNameString());
        string = string.replace("%e", npc.getNPC().getUnlocalizedName());
        string = string.replace("%$", "" + stats.getGold());

        if (npc.getLover() != null) {
            string = string.replace("%rE", npc.getLover().getNPC().getUnlocalizedName());
        } else string = string.replace("%rE", Translate.translate("nolover"));

        return string.replace("%rP", HFTrackers.getClientPlayerTracker().getRelationships().getLover());
    }

    public GuiNPCChat(EntityNPC npc, EntityPlayer player, int nextGui) {
        super(npc, player, nextGui);
        isScriptInit = false;
    }

    private boolean buildScript() {
        String[] original = WordUtils.wrap(format(getScript()), 39).split(SystemUtils.LINE_SEPARATOR);
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

        return true;
    }

    @Override
    public void drawForeground(int x, int y) {
        if (!isScriptInit) {
            isScriptInit = buildScript();
        }

        super.drawForeground(x, y);

        //Cancel the drawing if the script is null
        if (script == null) {
            endChat();
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
                fontRendererObj.drawStringWithShadow(text, 22, 158 + (i * 10), 0xFFFFFF);
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
                fontRendererObj.drawStringWithShadow(new String(fordisplay), 22, 158 + (line * 10), 0xFFFFFF);

                //Now if we have completed the entire array, let's reset the position and increase the line
                if (fordisplay.length >= todisplay.length) {
                    character = 0;
                    line++;
                }
            } else finished = true;
        }
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);
        if (key == 28 || key == 57 || character == 'q') {
            nextChat();
        }
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        nextChat();
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
            endChat();
        }
    }

    @Override
    public String getScript() {
        if (NPCHelper.isShopOpen(npc.getNPC(), player.worldObj, player) && nextGui == GuiHandler.SHOP_OPTIONS) {
            return npc.getNPC().getShop().getWelcome();
        }

        String script = HFTrackers.getClientPlayerTracker().getQuests().getScript(player, npc);
        return script == null ? npc.getNPC().getGreeting(player) : script;
    }
}
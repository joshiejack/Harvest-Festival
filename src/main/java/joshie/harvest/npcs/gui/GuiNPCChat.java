package joshie.harvest.npcs.gui;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.npcs.packet.PacketGift;
import joshie.harvest.npcs.packet.PacketInfo;
import joshie.harvest.player.stats.Stats;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** Renders a chat script **/
public class GuiNPCChat extends GuiNPCBase {
    private static final int MAX_LINES_PER_PAGE = 4;
    private String[][] script; //This is an array of [page][line], with line ALWAYS beign a length of MAX_LINES ^
    private int page; //Current page displayed
    private int line; //Current lines displayed
    private double character; //A ticker, Determines what character we should be displaying
    private boolean finished; //Whether the text has finished displaying
    private boolean isScriptInit = false;
    private boolean info = false;

    private String format(String string) {
        if (string == null) return "FORGOT SOME TEXT DUMBASS";
        Stats stats = HFTrackers.getClientPlayerTracker().getStats();
        String npcLover = npc.getLover() != null ? npc.getLover().getNPC().getLocalizedName() : TextHelper.translate("nolover");
        String playerLover = HFTrackers.getClientPlayerTracker().getRelationships().getLover();
        return String.format(string, stats.getGold(), playerLover, npcLover, player.getDisplayNameString(), npc.getNPC().getLocalizedName());
    }

    public GuiNPCChat(EntityPlayer player, EntityNPC npc, EnumHand hand, int nextGui, boolean info) {
        super(player, npc, hand, nextGui);
        isScriptInit = false;
        this.info = info;
    }

    private boolean buildScript() {
        List<String> formatted = fontRendererObj.listFormattedStringToWidth(format(getScript()), 172);
        String[] original = formatted.toArray(new String[formatted.size()]);
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

        return true;
    }

    @Override
    public void drawOverlay(int x, int y) {
        if (!isScriptInit) {
            isScriptInit = buildScript();
        }

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
                fontRendererObj.drawString(TextFormatting.BOLD + text, 20, 157 + (i * 10), 0xFFFFFF);
            }
        }

        //If the page we are trying to parse, has a string for the line we're trying to display
        if (line < MAX_LINES_PER_PAGE) {//If the current line, is less than the length of the lines, And we have less pages than max
            if (script[page][line] != null) {
                //Convert the next line in to a char array
                char[] todisplay = script[page][line].toCharArray();
                if (todisplay.length > 0) {
                    if (("" + todisplay[0]).equals("@")) {
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
                fontRendererObj.drawString(TextFormatting.BOLD + new String(fordisplay), 20, 157 + (line * 10), 0xFFFFFF);

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
        if ((npc.getNPC() == HFNPCs.GODDESS || isHoldingItem()) && hoveringGift())
            PacketHandler.sendToServer(new PacketGift(npc));
        else if (displayInfo() && hoveringInfo() && npc.getNPC().getInfoButton() != null)
            PacketHandler.sendToServer(new PacketInfo(npc));
        else nextChat();
    }

    protected void nextChat() {
        if (!finished) {
            finished = true;
            line = MAX_LINES_PER_PAGE;
        } else if (page < (script.length - 1)) {
            finished = false; //Reset the page being finished
            line = 0; //Reset the line we are currently reading
            page++; //Reset the page we are currently reading
        } else {
            endChat();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getScript() {
        //Shops
        if (NPCHelper.isShopOpen(npc, player.worldObj, player) && nextGui == GuiHandler.SHOP_OPTIONS) {
            return npc.getNPC().getShop(player.worldObj, pos).getWelcome(npc.getNPC());
        }

        //Info Greeting
        String infoGreeting = !info || npc.getNPC().getInfoButton() == null ? null : npc.getNPC().getInfoButton().getLocalizedText(player, npc, npc.getNPC());
        if (infoGreeting != null) return infoGreeting;

        //Scripts
        String script = QuestHelper.getScript(player, npc);
        return script == null ? npc.getNPC().getGreeting(player, npc) : script;
    }
}
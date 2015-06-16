package joshie.harvest.npc.gui;

import java.util.Arrays;
import java.util.HashSet;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTracker;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.ChatFontRenderer;
import joshie.harvest.core.util.GuiBase;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.player.PlayerStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;

public class GuiNPC extends GuiBase {
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODPATH, "textures/gui/chatbox.png");
    private static ResourceLocation name_texture;

    protected EntityPlayer player;
    protected EntityNPC npc;

    private static final int MAX_LINES_PER_PAGE = 3;
    protected String[][] script; //This is an array of [page][line], with line ALWAYS beign a length of MAX_LINES ^
    protected int page; //Current page displayed
    protected int line; //Current lines displayed
    protected double character; //A ticker, Determines what character we should be displaying
    protected boolean finished; //Whether the text has finished displaying
    protected boolean hasConfirmation; //Whether this page of the script has a cofnrimation screen
    protected boolean selectedBottom; //True if Yes is Selected, False if no is Selected

    public GuiNPC(EntityNPC npc, EntityPlayer player) {
        super(new ContainerNPC(npc, player.inventory), "chat", 0);

        this.xSize = 256;
        this.ySize = 256;
        this.npc = npc;
        this.player = player;
        this.hasInventory = false;
        this.character = 0;

        name_texture = new ResourceLocation(HFModInfo.MODPATH + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/" + npc.getNPC().getUnlocalizedName() + ".png");
        try {
            mc.renderEngine.getTexture(name_texture).loadTexture(mc.getResourceManager());
        } catch (Exception e) {
            name_texture = new ResourceLocation(HFModInfo.MODPATH + ":lang/en_US/" + npc.getNPC().getUnlocalizedName() + ".png");
        }
    }

    @Override
    public void drawBackground(int x, int y) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        
        mc.renderEngine.bindTexture(chatbox);
        drawTexturedModalRect(x, y + 150, 0, 150, 256, 46);
        ChatFontRenderer.colorise(npc.getNPC().getInsideColor());
        drawTexturedModalRect(x, y + 150, 0, 100, 256, 46);
        ChatFontRenderer.colorise(npc.getNPC().getOutsideColor());
        drawTexturedModalRect(x, y + 150, 0, 50, 256, 46);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        
        ChatFontRenderer.render(this, x, y, npc.getCommandSenderName(), npc.getNPC().getInsideColor(), npc.getNPC().getOutsideColor());
    }

    private String format(String string) {
        if (string == null) return "FORGOT SOME TEXT DUMBASS";
        PlayerStats stats = HFTracker.getPlayerTracker().getStats();
        string = string.replace("<BR>", SystemUtils.LINE_SEPARATOR);
        string = string.replace("Þ", player.getDisplayName());
        string = string.replace("ℇ", npc.getNPC().getUnlocalizedName());
        string = string.replace("$", "" + stats.getGold());

        if (npc.getLover() != null) {
            string = string.replace("�?�", npc.getLover().getNPC().getUnlocalizedName());
        } else string = string.replace("�?�", Translate.translate("nolover"));

        return string.replace("♥", HFTracker.getPlayerTracker().getRelationships().getLover());
    }

    protected void drawLines() {
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

    private void drawHeart(int value) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        int xPos = (int) ((((double) value / (Short.MAX_VALUE * 2))) * 7);
        drawTexturedModalRect(240, 130, 0, 0, 25, 25);
        drawTexturedModalRect(240, 130, 25 + (25 * xPos), 0, 25, 25);
    }

    @Override
    public void drawForeground(int x, int y) {
        if (npc == null) {
            player.closeScreen();
            HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
            for (IQuest quest : quests) {
                if (quest != null) {
                    quest.onClosedChat(player, npc);
                }
            }
        }

        mc.renderEngine.bindTexture(HFModInfo.elements);
        if (npc.getNPC().isMarriageCandidate()) {
            drawHeart(HFApi.RELATIONS.getAdjustedRelationshipValue(player, npc.getRelatable()));
        }

        if (hasConfirmation) {
            if (!selectedBottom) {
                drawTexturedModalRect(20, 169, 0, 32, 19, 8);
            } else {
                drawTexturedModalRect(20, 179, 0, 32, 19, 8);
            }
        }

        //If lines hasn't been initilised, get yourself a new passage
        if (script == null) {
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
        } else {
            drawLines();
        }
    }

    protected String getScript() {
        return HFTracker.getPlayerTracker().getQuests().getScript(player, npc);
    }

    @Override
    protected void keyTyped(char character, int key) {
        if (character == 'w' || key == 200) {
            selectedBottom = false;
        }

        if (character == 's' || key == 208) {
            selectedBottom = true;
        }

        if (key == 28) {
            select();
        }

        super.keyTyped(character, key);
    }

    private void select() {
        HashSet<IQuest> quests = QuestHelper.getCurrentQuest(player);
        for (IQuest quest : quests) {
            if (quest != null) {
                if (!selectedBottom) {
                    quest.confirm(player, npc);
                } else quest.cancel(player, npc);

                hasConfirmation = false;
                nextChat();
            }
        }
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
            endChat();
        }
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
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

    public void endChat() {
        player.closeScreen();
    }
}

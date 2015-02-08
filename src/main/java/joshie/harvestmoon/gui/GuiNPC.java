package joshie.harvestmoon.gui;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.RelationsHelper.getRelationshipValue;

import java.util.Arrays;
import java.util.HashSet;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.helpers.QuestHelper;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.player.PlayerDataClient;
import joshie.harvestmoon.quests.Quest;
import joshie.harvestmoon.util.Translate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;

public class GuiNPC extends GuiBase {
    private static ResourceLocation name_texture;

    private EntityPlayer player;
    private EntityNPC npc;

    private static final int MAX_LINES_PER_PAGE = 3;
    private String[][] script; //This is an array of [page][line], with line ALWAYS beign a length of MAX_LINES ^
    private int page; //Current page displayed
    private int line; //Current lines displayed
    private double character; //A ticker, Determines what character we should be displaying
    private boolean finished; //Whether the text has finished displaying

    public GuiNPC(EntityNPC npc, EntityPlayer player) {
        super(new ContainerNPC(npc, player.inventory), "chat", 0);

        this.xSize = 256;
        this.ySize = 256;
        this.npc = npc;
        this.player = player;
        this.hasInventory = false;
        this.character = 0;

        name_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/" + npc.getNPC().getUnlocalizedName() + ".png");
        try {
            mc.renderEngine.getTexture(name_texture).loadTexture(mc.getResourceManager());
        } catch (Exception e) {
            name_texture = new ResourceLocation(HMModInfo.MODPATH + ":lang/en_US/" + npc.getNPC().getUnlocalizedName() + ".png");
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        
        
    }

    @Override
    public void drawBackground(int x, int y) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(name_texture);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private String format(String string) {
        PlayerDataClient data = handler.getClient().getPlayerData();
        string = string.replace("<BR>", SystemUtils.LINE_SEPARATOR);
        string = string.replace("Ãž", player.getDisplayName());
        string = string.replace("â„‡", npc.getNPC().getUnlocalizedName());
        string = string.replace("$", "" + data.getGold());

        if (npc.getLover() != null) {
            string = string.replace("â�¤", npc.getLover().getNPC().getUnlocalizedName());
        } else string = string.replace("â�¤", Translate.translate("nolover"));

        return string.replace("â™¥", data.getLover());
    }

    private void drawLines() {
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
                if (character < todisplay.length) { //If the current position of the char array, is less than it's maximum
                    character += 0.2D; //Increase the tick, slowly
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

    }

    @Override
    public void drawForeground() {
        if (npc == null) {
            player.closeScreen();
            HashSet<Quest> quests = QuestHelper.getCurrentQuest(player);
            for (Quest quest : quests) {
                if (quest != null) {
                    quest.onClosedChat(player, npc);
                }
            }
        }

        drawHeart(getRelationshipValue(npc, player) + Short.MAX_VALUE);

        //If lines hasn't been initilised, get yourself a new passage
        if (script == null) {
            String[] original = WordUtils.wrap(format(handler.getClient().getPlayerData().getQuests().getScript(player, npc)), 39).split(SystemUtils.LINE_SEPARATOR);
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

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        if (!finished) {
            finished = true;
            line = 3;
        } else if (finished && page < (script.length - 1)) {
            finished = false; //Reset the page being finished
            line = 0; //Reset the line we are currently reading
            page++; //Reset the page we are currently reading
        } else {
            player.closeScreen();
        }
    }
}

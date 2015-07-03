package joshie.harvest.npc.gui;

import java.util.HashSet;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.QuestHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.ChatFontRenderer;
import joshie.harvest.core.util.GuiBase;
import joshie.harvest.npc.NPCScriptHandler;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiNPC extends GuiBase {
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODPATH, "textures/gui/chatbox.png");
    protected EntityPlayer player;
    protected EntityNPC npc;
    protected NPCScriptHandler theScript;

    public GuiNPC(EntityNPC npc, EntityPlayer player) {
        super(new ContainerNPC(npc, player.inventory), "chat", 0);

        this.xSize = 256;
        this.ySize = 256;
        this.npc = npc;
        this.player = player;
        this.hasInventory = false;
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


        
        if (theScript == null || theScript.executed()) {
            theScript = NPCHelper.getScript(player, npc.getNPC(), theScript).start(this, player, npc);
        }
        
        if (theScript == null) {
            endChat();
        }
        
        theScript.draw(this, fontRendererObj);
    }

    public String getScript() {
        //IShop shop = npc.getNPC().getShop();
        //if(shop != null && shop.isOpen(player.worldObj, player) && shop.getContents(player).size() > 0) {
            //return shop.getWelcome();
        //}
        
        String script = HFTrackers.getClientPlayerTracker().getQuests().getScript(player, npc);
        return script == null ? npc.getNPC().getGreeting() : script;
    }

    @Override
    protected void keyTyped(char character, int key) {
        theScript.keyTyped(character, key);
        super.keyTyped(character, key);
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        theScript.onMouseClick(mouseX, mouseY);
    }

    public void endChat() {
        player.closeScreen();
    }
}

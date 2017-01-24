package joshie.harvest.npcs.gui;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class GuiNPCMask extends GuiNPCChat {
    private final Script script;
    private NPC posingAs;

    public GuiNPCMask(EntityPlayer player, EntityNPC npc, int scriptID) {
        super(player, npc, EnumHand.MAIN_HAND, -1, false);
        script = Script.REGISTRY.getValues().get(scriptID);
        posingAs = script.getNPC() != null ? script.getNPC() : npc.getNPC();
        inside = posingAs.getInsideColor();
        outside = posingAs.getOutsideColor();
    }

    @Override
    protected void drawName(int x, int y) {
        ChatFontRenderer.render(this, x, y, posingAs.getLocalizedName(), inside, outside);
    }

    @Override
    protected void drawTabs(int x, int y) {}

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        nextChat();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getScript() {
        return script.getLocalized();
    }
}
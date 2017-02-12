package joshie.harvest.npcs.gui;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class GuiNPCMask extends GuiNPCChat {
    private final Script script;
    private NPC posingAs;

    public GuiNPCMask(EntityPlayer player, EntityNPC npc, int scriptID) {
        super(player, npc, -1, false);
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
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        if (mouseButton == 0) nextChat();
        else if (mouseButton == 1) previousChat();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getScript() {
        return script.getLocalized(npc, posingAs);
    }
}
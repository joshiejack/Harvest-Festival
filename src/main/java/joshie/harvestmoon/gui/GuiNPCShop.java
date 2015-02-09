package joshie.harvestmoon.gui;

import joshie.harvestmoon.entities.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public class GuiNPCShop extends GuiNPC {
    private boolean welcome;

    public GuiNPCShop(EntityNPC npc, EntityPlayer player) {
        super(npc, player);
    }

    @Override
    public void drawBackground(int x, int y) {
        if (!welcome) {
            super.drawBackground(x, y);
        }
    }

    @Override
    public void drawForeground() {
        if (!welcome) {
            super.drawForeground();
        }
    }

    @Override
    public void endChat() {
        welcome = true;
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        if (!welcome) {
            super.onMouseClick(mouseX, mouseY);
        }
    }
}

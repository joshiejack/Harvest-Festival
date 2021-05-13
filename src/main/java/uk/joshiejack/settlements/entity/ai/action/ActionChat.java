package uk.joshiejack.settlements.entity.ai.action;

import uk.joshiejack.settlements.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public interface ActionChat {
    void onGuiClosed(EntityPlayer player, EntityNPC npc, Object... parameters);
}

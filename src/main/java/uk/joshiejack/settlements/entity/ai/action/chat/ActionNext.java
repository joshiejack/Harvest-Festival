package uk.joshiejack.settlements.entity.ai.action.chat;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionChat;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.event.NPCEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.MinecraftForge;

@PenguinLoader("next")
public class ActionNext extends ActionMental implements ActionChat {
    @Override
    public EnumActionResult execute(EntityNPC npc) {
        return  player == null ||
                !MinecraftForge.EVENT_BUS.post(new NPCEvent.NPCRightClickedEvent(npc, player, player.getActiveHand()))
                ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    @Override
    public void onGuiClosed(EntityPlayer player, EntityNPC npc, Object... parameters) {}
}

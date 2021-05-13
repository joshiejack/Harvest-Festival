package uk.joshiejack.settlements.entity.ai.action.chat;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionChat;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.network.npc.PacketGreet;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("greet")
public class ActionGreet extends ActionMental implements ActionChat {
    private boolean read;
    private boolean displayed;

    @Override
    public void onGuiClosed(EntityPlayer player, EntityNPC npc, Object... parameters) {
        read = true;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (!displayed && player != null) {
            displayed = true; //Marked it as displayed
            npc.addTalking(player); //Add the talking
            PenguinNetwork.sendToClient(new PacketGreet(player, npc, this), player);
        }

        return player == null || npc.IsNotTalkingTo(player) || read ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }
}

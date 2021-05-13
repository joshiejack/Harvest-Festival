package uk.joshiejack.settlements.entity.ai.action.chat;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("look")
public class ActionLook extends ActionMental {
    private int lookTimer;

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            npc.getLookHelper().setLookPosition(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ, (float) npc.getHorizontalFaceSpeed(), (float) npc.getVerticalFaceSpeed());
        }

        lookTimer++;

        return lookTimer > 40? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }
}

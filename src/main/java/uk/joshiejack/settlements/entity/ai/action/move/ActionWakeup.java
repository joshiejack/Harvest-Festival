package uk.joshiejack.settlements.entity.ai.action.move;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("wakeup")
public class ActionWakeup extends ActionPhysical {
    @Override
    public EnumActionResult execute(EntityNPC npc) {
        npc.setAnimation("wakeup");
        return EnumActionResult.SUCCESS;
    }
}

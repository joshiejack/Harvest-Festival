package uk.joshiejack.settlements.client.animation;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("wakeup")
public class AnimationWake extends Animation {
    @Override
    public void play(EntityNPC npc) {
        npc.renderOffsetX = 0;
        npc.renderOffsetZ = 0;
    }
}

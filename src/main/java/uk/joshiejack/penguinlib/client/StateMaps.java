package uk.joshiejack.penguinlib.client;

import uk.joshiejack.penguinlib.block.base.BlockMultiSapling;
import net.minecraft.client.renderer.block.statemap.StateMap;

public class StateMaps {
    public static final StateMap NO_STAGES = new StateMap.Builder().ignore(BlockMultiSapling.STAGE).build();
}

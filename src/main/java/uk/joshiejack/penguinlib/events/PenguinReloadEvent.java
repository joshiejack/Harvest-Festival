package uk.joshiejack.penguinlib.events;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PenguinReloadEvent extends WorldEvent {
    public PenguinReloadEvent(World world) {
        super(world);
    }
}

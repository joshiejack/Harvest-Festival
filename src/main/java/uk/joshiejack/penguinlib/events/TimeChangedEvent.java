package uk.joshiejack.penguinlib.events;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class TimeChangedEvent extends WorldEvent {
    private final long time;

    public TimeChangedEvent(World world, long time) {
        super(world);
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}

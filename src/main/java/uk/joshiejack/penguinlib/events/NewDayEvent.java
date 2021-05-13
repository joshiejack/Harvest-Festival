package uk.joshiejack.penguinlib.events;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

public class NewDayEvent extends WorldEvent {
    public NewDayEvent(WorldServer world) {
        super(world);
    }

    @Override
    public WorldServer getWorld() {
        return (WorldServer) super.getWorld();
    }
}

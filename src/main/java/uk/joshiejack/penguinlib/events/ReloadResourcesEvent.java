package uk.joshiejack.penguinlib.events;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class ReloadResourcesEvent extends WorldEvent {
    public ReloadResourcesEvent(World world) {
        super(world);
    }
}

package uk.joshiejack.penguinlib.events;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.UUID;

public class TeamChangedEvent extends WorldEvent {
    private final UUID player;
    private final UUID oldTeam;
    private final UUID newTeam;

    public TeamChangedEvent(World world,  UUID player, UUID oldTeam, UUID newTeam) {
        super(world);
        this.player = player;
        this.oldTeam = oldTeam;
        this.newTeam = newTeam;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getOldTeam() {
        return oldTeam;
    }

    public UUID getNewTeam() {
        return newTeam;
    }
}

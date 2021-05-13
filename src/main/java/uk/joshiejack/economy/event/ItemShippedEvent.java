package uk.joshiejack.economy.event;

import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class ItemShippedEvent extends WorldEvent {
    private final PenguinTeam team;
    private final ItemStack shipped;
    private final long value;

    public ItemShippedEvent(World world, PenguinTeam team, ItemStack shipped, long value) {
        super(world);
        this.team = team;
        this.shipped = shipped;
        this.value = value;
    }

    public PenguinTeam getTeam() {
        return team;
    }

    public ItemStack getShipped() {
        return shipped;
    }

    public long getValue() {
        return value;
    }
}

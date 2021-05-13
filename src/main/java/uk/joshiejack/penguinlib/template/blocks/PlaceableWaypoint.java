package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
@PenguinLoader("waypoint")
public class PlaceableWaypoint extends PlaceableDecorative {
    private String name;

    public PlaceableWaypoint() {}
    public PlaceableWaypoint(String name, BlockPos offset) {
        super(Blocks.AIR.getDefaultState(), offset);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
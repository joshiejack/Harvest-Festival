package uk.joshiejack.harvestcore.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;


public class GatherTownCentres extends WorldEvent {
    private final List<BlockPos> townCentres;

    /**
     * This event is used to gather the centre of what is considered a living space
     *  As well as the radius around that centre that should count as wilderness
     *
     * @param   world           the world we are gathering locations for
     * @param   townCentres     a list of positions that are considered the centre
     *                          of a civilized area. Notably vanilla villages and
     *                          settlements villages
     */
    public GatherTownCentres(World world, List<BlockPos> townCentres) {
        super(world);
        this.townCentres = townCentres;
    }

    /**
     * Call this to add a town centre to the list of town centres
     *
     * @param townCentre    a block location considered a centre of civilization
     */
    public void add(BlockPos townCentre) {
        this.townCentres.add(townCentre);
    }
}

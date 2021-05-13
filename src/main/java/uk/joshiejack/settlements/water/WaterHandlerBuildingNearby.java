package uk.joshiejack.settlements.water;

import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.harvestcore.water.WaterHandler;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterHandlerBuildingNearby extends WaterHandler {
    private final Building building;
    private final int distance;

    private WaterHandlerBuildingNearby(String name, Building building, int distance, boolean match) {
        super(name, match);
        this.building = building;
        this.distance = distance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isType(World world, BlockPos pos) {
        Town<?> town = TownFinder.find(world, pos);
        TownBuilding location = town.getLandRegistry().getBuildingLocation(building);
        return location.getCentre().getDistance(pos.getX(), pos.getY(), pos.getZ()) <= distance;
    }

    @PenguinLoader("building")
    public static class Builder extends WaterHandler.Builder {
        @Override
        public WaterHandler build(String name, String data) {
            Building building = Building.REGISTRY.get(new ResourceLocation(data.split(";")[0]));
            int distance = Integer.parseInt(data.split(";")[1]);
            return new WaterHandlerBuildingNearby(name, building, distance, data.startsWith("!"));
        }
    }
}

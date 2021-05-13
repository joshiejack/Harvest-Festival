package uk.joshiejack.settlements.data.custom.building;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("building:standard")
public class CustomBuildingData extends AbstractCustomBuildingData<CustomBuildingData> {
    @Nonnull
    @Override
    public Building build(ResourceLocation registryName, @Nonnull CustomBuildingData main, @Nullable CustomBuildingData... data) {
        return build(registryName, main);
    }
}

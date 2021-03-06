package uk.joshiejack.settlements.data.custom.building;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("building:upgrade")
public class CustomBuildingUpgradeData extends AbstractCustomBuildingData<CustomBuildingUpgradeData> {
    public String requirement;
    public boolean overrides;

    @Nonnull
    @Override
    public Building build(ResourceLocation registryName, @Nonnull CustomBuildingUpgradeData main, @Nullable CustomBuildingUpgradeData... data) {
        return build(registryName, main).setUpgrade(new ResourceLocation(main.requirement), main.overrides);
    }
}

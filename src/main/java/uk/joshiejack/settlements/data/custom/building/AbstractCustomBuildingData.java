package uk.joshiejack.settlements.data.custom.building;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.Template;

public abstract class AbstractCustomBuildingData<T extends AbstractCustomBuildingData<?>> extends AbstractCustomData<Building, T> {
    public BlockPos offset = BlockPos.ORIGIN;
    public int limit = 1;
    public Placeable[] components;

    protected Building build(ResourceLocation registryName, T main) {
        Building building = new Building(registryName, Template.wrap(main.components));
        if (!main.offset.equals(BlockPos.ORIGIN)) building.setOffset(main.offset);
        if (main.limit <= 1) building.setLimit(1);
        else building.setLimit(limit);
        if (main.getScript() != null) {
            building.setDailyHandler(main.getScript());
        }

        return building;
    }
}

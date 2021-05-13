package uk.joshiejack.settlements.scripting.wrappers;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PositionJS;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TownBuildingJS extends AbstractJS<TownBuilding> {
    public TownBuildingJS(TownBuilding location) {
        super(location);
    }

    public boolean is(String building) {
        return penguinScriptingObject.getBuilding().getRegistryName().toString().equals(building);
    }

    public List<PositionJS> waypointsByPrefix(String name) {
        List<BlockPos> list = penguinScriptingObject.getWaypointsByPrefix(name);
        List<PositionJS> positions = Lists.newArrayList();
        list.forEach(p -> positions.add(WrapperRegistry.wrap(p)));
        return positions;
    }

    public Rotation rotation() {
        return penguinScriptingObject.getRotation();
    }

    public PositionJS pos() {
        return WrapperRegistry.wrap(penguinScriptingObject.getPosition());
    }
}

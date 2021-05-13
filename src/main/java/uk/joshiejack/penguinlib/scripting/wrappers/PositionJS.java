package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PositionJS extends AbstractJS<BlockPos> {
    public PositionJS(BlockPos pos) {
        super(pos);
    }

    public PositionJS offset(int x, int y, int z) {
        return WrapperRegistry.wrap(penguinScriptingObject.add(x, y, z));
    }

    public PositionJS offset(EnumFacing facing, int amount) {
        return WrapperRegistry.wrap(penguinScriptingObject.offset(facing, amount));
    }

    public PositionJS north() {
        return WrapperRegistry.wrap(penguinScriptingObject.north());
    }

    public PositionJS east() {
        return WrapperRegistry.wrap(penguinScriptingObject.east());
    }

    public PositionJS south() {
        return WrapperRegistry.wrap(penguinScriptingObject.south());
    }

    public PositionJS west() {
        return WrapperRegistry.wrap(penguinScriptingObject.west());
    }

    public PositionJS up() {
        return WrapperRegistry.wrap(penguinScriptingObject.up());
    }

    public PositionJS down() {
        return WrapperRegistry.wrap(penguinScriptingObject.down());
    }

    public int x() {
        return penguinScriptingObject.getX();
    }

    public int y() {
        return penguinScriptingObject.getY();
    }

    public int z() {
        return penguinScriptingObject.getZ();
    }
}

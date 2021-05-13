package uk.joshiejack.penguinlib.scripting.wrappers;

import com.google.common.base.Optional;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.apache.logging.log4j.util.Strings;

public class StateJS extends AbstractJS<IBlockState> {
    public StateJS(IBlockState state) {
        super(state);
    }

    public ItemStackJS item() {
        return WrapperRegistry.wrap(StackHelper.getStackFromState(penguinScriptingObject));
    }

    public boolean is(String state) {
        return penguinScriptingObject == StateAdapter.fromString(state);
    }

    public boolean is(String propertyName, String value) {
        IBlockState state = penguinScriptingObject;
        IProperty<?> theProperty = state.getBlock().getBlockState().getProperty(propertyName);
        if (theProperty != null) {
            Optional<? extends Comparable> optional = theProperty.parseValue(value);
            if (optional.isPresent()) {
                Comparable<?> targetProperty = optional.get();
                Comparable<?> thisProperty = state.getValue(theProperty);
                return targetProperty.equals(thisProperty);
            } else return false;
        } else return false;
    }

    public String block() {
        return penguinScriptingObject.getBlock().getRegistryName().toString();
    }

    @SuppressWarnings("unchecked")
    public boolean isLeaves(WorldJS<?> worldW, PositionJS posW) {
        return penguinScriptingObject.getBlock().isLeaves(penguinScriptingObject, worldW.penguinScriptingObject, posW.penguinScriptingObject);
    }

    @SuppressWarnings("unchecked")
    public String property(String name) {
        IBlockState object = penguinScriptingObject;
        IProperty property = object.getBlock().getBlockState().getProperty(name);
        return property != null ? property.getName(object.getValue(property)) : Strings.EMPTY;
    }
}

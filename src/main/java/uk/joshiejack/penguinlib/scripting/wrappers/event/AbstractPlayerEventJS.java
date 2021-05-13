package uk.joshiejack.penguinlib.scripting.wrappers.event;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AbstractPlayerEventJS<C extends PlayerEvent> extends AbstractEventJS<C> {
    public AbstractPlayerEventJS(C object) {
        super(object);
    }

    public PlayerJS player() {
        return WrapperRegistry.wrap(penguinScriptingObject.getEntityPlayer());
    }
}

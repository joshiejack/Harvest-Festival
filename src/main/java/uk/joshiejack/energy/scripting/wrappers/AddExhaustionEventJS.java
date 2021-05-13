package uk.joshiejack.energy.scripting.wrappers;

import uk.joshiejack.energy.events.AddExhaustionEvent;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.scripting.wrappers.event.AbstractEventJS;

public class AddExhaustionEventJS extends AbstractEventJS<AddExhaustionEvent> {
    public AddExhaustionEventJS(AddExhaustionEvent object) {
        super(object);
    }

    public PlayerJS player() {
        return WrapperRegistry.wrap(event().getEntityPlayer());
    }

    public float getExhaustion() {
        return event().getExhaustion();
    }

    public float getNewValue() {
        return event().getNewValue();
    }

    public void setNewValue(float newValue) {
        event().setNewValue(newValue);
    }
}

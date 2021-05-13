package uk.joshiejack.penguinlib.scripting.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import javax.script.Bindings;
import java.util.Locale;

public class CollectScriptingBindings extends Event {
    private final Bindings bindings;

    public CollectScriptingBindings(Bindings bindings) {
        this.bindings = bindings;
    }

    public Bindings getBindings() {
        return bindings;
    }

    public <E extends Enum<E>> void registerEnum(Class<E> e) {
        for (E v: e.getEnumConstants()) {
            bindings.put(v.name().toLowerCase(Locale.ENGLISH), v);
        }
    }
}

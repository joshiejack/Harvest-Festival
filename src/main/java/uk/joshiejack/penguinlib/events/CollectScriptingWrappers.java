package uk.joshiejack.penguinlib.events;

import uk.joshiejack.penguinlib.scripting.Builder;
import uk.joshiejack.penguinlib.scripting.Sandbox;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class CollectScriptingWrappers extends Event {
    private final List<Pair<Class<?>, Builder<?, ?>>> extensibles;
    private final Map<Class<?>, Builder<?, ?>> statics;

    public CollectScriptingWrappers(List<Pair<Class<?>, Builder<?, ?>>> extensibles, Map<Class<?>, Builder<?, ?>> statics) {
        this.extensibles = extensibles;
        this.statics = statics;
    }

    public <C, T extends AbstractJS<C>> void register (Class<T> wrapper) {
        Sandbox.allow(wrapper.getName());
    }

    public <C, T extends AbstractJS<C>> Builder<C, T> registerExtensible(Class<T> wrapper, Class<C> clazz) {
        Builder<C, T> builder = new Builder<>(wrapper, clazz);
        Sandbox.allow(wrapper.getName());
        extensibles.add(Pair.of(clazz, builder));
        return builder;
    }

    public <C, T extends AbstractJS<C>> Builder<C, T> register (Class<T> wrapper, Class<C> clazz) {
        Builder<C, T> builder = new Builder<>(wrapper, clazz);
        Sandbox.allow(wrapper.getName());
        statics.put(clazz, builder);
        return builder;
    }
}

package uk.joshiejack.penguinlib.scripting;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public class Builder<C, R extends AbstractJS<C>> {
    private final Cache<C, R> serverCache = CacheBuilder.newBuilder().build();
    private final Cache<C, R> clientCache = CacheBuilder.newBuilder().build();
    private final Class<C> parameter;
    private final Class<R> wrapper;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean dynamic;
    private boolean disabled;
    private boolean sided;

    public Builder(Class<R> wrapper, Class<C> parameter) {
        this.wrapper = wrapper;
        this.parameter = parameter;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public Builder<C, R> setPriority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    public boolean isEnabled() {
        return !disabled;
    }

    public Builder<C, R> disable() {
        this.disabled = true;
        return this;
    }

    public Builder<C, R> setDynamic() {
        this.dynamic = true;
        return this;
    }

    public Builder<C, R> setSided() {
        this.sided = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public R wrap(Object object) {
        try {
            if (dynamic) return wrapper.getConstructor(parameter).newInstance(object);
            Cache<C, R> cache = sided && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? clientCache : serverCache;
            return cache.get((C) object, () -> wrapper.getConstructor(parameter).newInstance(object));
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package uk.joshiejack.penguinlib.scripting;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.wrappers.AbstractJS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class WrapperRegistry {
    private static final Cache<Object[], Object[]> WRAPPED = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    private static final List<Pair<Class<?>, Builder<?, ?>>> EXTENSIBLES = Lists.newArrayList();
    private static final Map<Class<?>, Builder<?, ?>> STATICS = Maps.newHashMap();

    @SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        MinecraftForge.EVENT_BUS.post(new CollectScriptingWrappers(EXTENSIBLES, STATICS));
        EXTENSIBLES.sort(Comparator.comparing(p -> p.getValue().getPriority()));
    }

    @SuppressWarnings("unchecked")
    public static <O> O unwrap(Object o) {
        return o instanceof AbstractJS ? (O) ((AbstractJS<?>) o).penguinScriptingObject : (O) o;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <R extends AbstractJS<?>> R wrap(Object object) {
        if (STATICS.containsKey(object.getClass())) {
            return (R) Objects.requireNonNull(STATICS.get(object.getClass()).wrap(object));
        } else {
            Optional<? extends Builder<?, ?>> b = EXTENSIBLES.stream().filter(w -> w.getLeft().isAssignableFrom(object.getClass())).map(Pair::getRight).findFirst();
            return Objects.requireNonNull(b.map(builder -> (R) Objects.requireNonNull(builder.wrap(object))).orElse(null));
        }
    }

    private static Object[] doWrap(Object... objects) {
        Object[] ret = new Object[objects.length];
        for (int i = 0; i < objects.length; i++) {
            if (objects[0] instanceof AbstractJS) {
                ret[i] = objects[i];
            } else {
                Class<?> clazz = objects[i].getClass();
                if (STATICS.containsKey(clazz) && STATICS.get(objects[i].getClass()).isEnabled()) {
                    ret[i] = STATICS.get(objects[i].getClass()).wrap(objects[i]);

                } else {
                    Object o = objects[i];
                    Optional<? extends Builder<?, ?>> b = EXTENSIBLES.stream().filter(w ->
                            w.getLeft().isAssignableFrom(o.getClass()) && w.getRight().isEnabled())
                            .map(Pair::getRight).findFirst();
                    if (b.isPresent()) {
                        ret[i] = b.get().wrap(o);
                    } else {
                        ret[i] = objects[i];
                    }
                }
            }
        }

        return ret;
    }

    static Object[] wrapAll(Object... objects) {
        try {
            return WRAPPED.get(objects, () -> doWrap(objects));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return doWrap(objects);
        }
    }
}

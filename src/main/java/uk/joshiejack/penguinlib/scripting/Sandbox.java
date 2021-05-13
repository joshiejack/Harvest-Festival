package uk.joshiejack.penguinlib.scripting;

import com.google.common.collect.Sets;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Sandbox {
    private static final Set<String> ALLOWED = Sets.newHashSet();

    public static boolean exposeToScripts(String s) {
        return ALLOWED.contains(s);
    }

    public static void allow(String clazz) {
        ALLOWED.add(clazz);
    }

    @SuppressWarnings({"SuspiciousInvocationHandlerImplementation"})
    public static ScriptEngine get() {
        ScriptEngine engine = null;
        HashMap<String, Class<?>> usedClasses = new HashMap<>();
        ScriptEngineManager manager = new ScriptEngineManager(null);
        ScriptEngineFactory factory = manager.getEngineFactories().stream()
                .filter(f -> f.getEngineName().endsWith("Nashorn")).collect(Collectors.toList()).get(0);

        for (Method m : factory.getClass().getDeclaredMethods())
            for (Class<?> c : m.getParameterTypes())
                usedClasses.put(c.getName(), c);

        Class<?> classFilter = usedClasses.get("jdk.nashorn.api.scripting.ClassFilter");
        if (classFilter != null) {
            Object classFilterInstance = Proxy.newProxyInstance(classFilter.getClassLoader(),
                    new Class<?>[]{classFilter},
                    (proxy, method, args) -> method.getName().equals("exposeToScripts")
                            && exposeToScripts((String) args[0]));
            try {
                Method getScriptEngine = factory.getClass().getMethod("getScriptEngine", classFilter);
                engine = (ScriptEngine) getScriptEngine.invoke(factory, classFilterInstance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return engine;
    }
}
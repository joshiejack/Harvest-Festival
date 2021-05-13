package uk.joshiejack.penguinlib.scripting;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.Strings;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingBindings;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.script.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Interpreter {
    protected static final ScriptEngine engine = Sandbox.get();
    protected static final Bindings bindings = engine.createBindings();
    protected static String functions = Strings.EMPTY;
    private static final Invocable inv = (Invocable) engine;
    private final Cache<String, Boolean> hasMethod = CacheBuilder.newBuilder().build();
    protected final ScriptContext context;
    protected final String javascript;
    protected final Object scriptLocation;
    protected String subdir;
    public ResourceLocation this_id;

    public Interpreter(@Nonnull Object scriptLocation) {
        this.context = new SimpleScriptContext();
        this.scriptLocation = scriptLocation;
        String parsed = getJavaScript(scriptLocation);
        this.javascript = parsed.contains("penguinScriptingObject") ? Strings.EMPTY : parsed;
        this.subdir = "scripts";
    }

    protected static void initGlobal() {
        if (bindings.size() == 0)  MinecraftForge.EVENT_BUS.post(new CollectScriptingBindings(bindings));
        if (StringUtils.isNullOrEmpty(functions)) functions = getScriptingFunctions();
    }

    public Interpreter init(ResourceLocation registryName) {
        initGlobal();
        this_id = registryName;
        try {
            context.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
            context.setAttribute("this_id", registryName, ScriptContext.ENGINE_SCOPE);
            engine.eval(functions, context);
            engine.eval(javascript, context);
        } catch (ScriptException | StackOverflowError e) {
            PenguinLib.logger.log(Level.ERROR, "Issue with loading in the script @ " + registryName);
            e.printStackTrace();
        }

        return this;
    }

    public String getLocalizedKey(ResourceLocation registryName, String key) {
        return registryName.getNamespace() + "." + getPath() + "." + registryName.getPath() + "." + key;
    }

    public String getDir() {
        return getPath() + "s";
    }

    public String getPath() {
        return "script";
    }

    public Object getLocation() {
        return scriptLocation;
    }

    public String getJavaScript(@Nonnull Object scriptLocation) { //Pluralise it for the directory ;)
        return scriptLocation instanceof String ? (String) scriptLocation : scriptLocation instanceof ResourceLocation ? ResourceLoader.getJavaScriptResource((ResourceLocation) scriptLocation, getDir()) : ResourceLoader.getStringFromFile((File) scriptLocation);
    }

    public Interpreter copy(ResourceLocation registryName) {
        return new Interpreter(scriptLocation).init(registryName);
    }

    protected static String getScriptingFunctions() {
        List<String> strings = Lists.newArrayList();
        MinecraftForge.EVENT_BUS.post(new CollectScriptingFunctions(strings));
        return String.join("", strings);
    }

    public boolean hasMethod(String string) {
        try {
            return hasMethod.get(string, () -> {
                StringBuilder builder = new StringBuilder();
                Arrays.stream(javascript.split("\\r?\\n"))
                        .filter(s -> s.startsWith("include"))
                        .forEach(s2 -> {
                            ResourceLocation location = new ResourceLocation(s2.replaceFirst("include\\('", "")
                                    .replaceFirst("\\'\\)", "").replace(";", "").replaceFirst(subdir + "/", ""));

                            builder.append(ResourceLoader.getJavaScriptResource(location, subdir));
                        });

                return builder.append("\n").append(javascript).toString().contains("function " + string);
            });
        } catch (ExecutionException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <R> R getValue(String function, R default_, Object... parameters) {
        Object o = getResultOfFunction(function, parameters);
        return o == null ? default_ : (R) o;
    }

    @Nullable
    public Object getResultOfFunction(String function, Object... parameters){
        if (!function.equals("typeof") && !hasMethod(function)) return null; //Don't bother continuing if we don't have the function
        try {
            engine.setContext(context);
            return inv.invokeFunction(function, WrapperRegistry.wrapAll(parameters));
        } catch (ScriptException ex) {
            PenguinLib.logger.log(Level.ERROR, "Scripting issue with the quest: " + context.getAttribute("this_id"));
            ex.printStackTrace();
            return null;
        } catch (NoSuchMethodException ignored) { return null; }
    }

    public boolean callFunction(String function, Object... parameters) {
        return getResultOfFunction(function, parameters) != null;
    }

    public boolean isTrue(String function, Object... parameters) {
        Object result = getResultOfFunction(function, parameters);
        return result instanceof Boolean && ((boolean)result);
    }
}

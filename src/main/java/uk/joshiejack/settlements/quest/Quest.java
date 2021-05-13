package uk.joshiejack.settlements.quest;

import com.google.common.collect.Maps;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.settlements.quest.settings.Settings;
import uk.joshiejack.settlements.scripting.wrappers.QuestJS;
import uk.joshiejack.settlements.scripting.wrappers.QuestTrackerJS;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Quest extends PenguinRegistry {
    public static final Map<ResourceLocation, Quest> REGISTRY = Maps.newHashMap();
    private final Map<String, String> methodToFire = new HashMap<>();
    private final Interpreter interpreter;
    private final Settings settings;

    public Quest(Object scriptLocation, ResourceLocation registryName) {
        super(REGISTRY, registryName);
        this.settings = new Settings();
        this.interpreter = new Interpreter(this, scriptLocation).init(registryName);
    }

    public Quest copy() {
        return new Quest(interpreter.getLocation(), getRegistryName());
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public Settings getSettings() {
        return settings;
    }

    public Set<String> getTriggers() {
        return methodToFire.keySet();
    }

    //Fire the relevant method
    public void fire(String method, EntityPlayer player, QuestTracker tracker) {
        String function = method.equals("canStart") ? "canStart" : methodToFire.get(method); //This is the function we have to call
        if (function.equals("canStart")) {
            //If we are the canStart function get ready to mark the script as active
            if (Scripting.getResult(interpreter, function, true, player, new QuestTrackerJS(tracker))) {
                tracker.start(this);
            }
        } else interpreter.callFunction(function, player, new QuestTrackerJS(tracker));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest script = (Quest) o;
        return Objects.equals(getRegistryName(), script.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public static class Interpreter extends uk.joshiejack.penguinlib.scripting.Interpreter {
        private final Quest quest;

        public Interpreter(@Nonnull Quest quest, @Nonnull Object scriptLocation) {
            super(scriptLocation);
            this.quest = quest;
            this.subdir = "quests";
        }

        @Override
        public String getPath() {
            return "quest";
        }

        @Override
        public Interpreter copy(ResourceLocation registryName) {
            return new Interpreter(quest, scriptLocation).init(registryName);
        }

        @Override
        public Interpreter init(ResourceLocation registryName) {
            initGlobal();
            this_id = new ResourceLocation("quest", quest.getRegistryName().toString());
            try {
                context.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
                context.setAttribute("quest", new QuestJS(quest), ScriptContext.ENGINE_SCOPE);
                context.setAttribute("this_id", this_id, ScriptContext.ENGINE_SCOPE);
                engine.eval(functions, context);
                engine.eval(javascript, context);

                callFunction("setup", quest.getSettings());
                String commaList = quest.getSettings().getTriggers();
                String[] commaSplit = commaList.replace(" ", "").split(",");
                for (String c : commaSplit) {
                    if (c.contains(":")) {
                        String method = c.split(":")[0];
                        String function = c.split(":")[1];
                        quest.methodToFire.put(method, function);
                    } else quest.methodToFire.put(c, "canStart");
                }

                //"Forbidden Keyword >>> Unwrap"
            } catch (ScriptException | StackOverflowError e) {
                PenguinLib.logger.log(Level.ERROR, "Issue with loading in the quest @ " + registryName);
                e.printStackTrace();
            }

            return this;
        }
    }
}

package uk.joshiejack.penguinlib.scripting.event;

import uk.joshiejack.penguinlib.scripting.Sandbox;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class CollectScriptingFunctions extends Event {
    private final List<String> list;

    public CollectScriptingFunctions(List<String> list) {
        this.list = list;
    }

    public void registerVar(String var, Class clazz) {
       Sandbox.allow(clazz.getName());
       list.add("var " + var + " = Java.type('" + clazz.getName() + "');");
    }

    public void registerFunction(String name, String code) {
        list.add("function " + name + " { " + code + " }");
    }
}

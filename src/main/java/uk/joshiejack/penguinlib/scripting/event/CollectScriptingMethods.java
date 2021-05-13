package uk.joshiejack.penguinlib.scripting.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class CollectScriptingMethods extends Event {
    private final List<String> list;

    public CollectScriptingMethods(List<String> list) {
        this.list = list;
    }

    public void add(String string) {
        list.add(string);
    }
}

package uk.joshiejack.penguinlib.events;

import uk.joshiejack.penguinlib.PenguinCommon;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;

public class CollectRegistryEvent extends Event {
    private final Map<Class, PenguinCommon.PenguinRegister> map;

    public CollectRegistryEvent(Map<Class, PenguinCommon.PenguinRegister> map) {
        this.map = map;
    }

    public <T> void add(Class<T> clazz, PenguinCommon.PenguinRegister<T> r) {
        this.map.put(clazz, r);
    }
}

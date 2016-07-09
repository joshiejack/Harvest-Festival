package joshie.harvest.debug;

import joshie.harvest.HarvestFestival;
import org.apache.logging.log4j.Level;

import java.util.HashMap;

public class HFDebug {
    public static void preInit() {}

    private static class Data {
        String name;
        int amount;

        public Data(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }

    public static HashMap<String, Long> timers = new HashMap<String, Long>();

    public static void start(String name) {
        timers.put(name, System.nanoTime());
    }

    public static void end(String name) {
        long current = System.nanoTime();
        long then = timers.get(name);
        HarvestFestival.LOGGER.log(Level.INFO, name + " took " + (current - then));
    }
}

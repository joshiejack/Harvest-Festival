package uk.joshiejack.settlements.npcs.status;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Status {
    private static final Object2IntMap<String> RESET = new Object2IntOpenHashMap<>();

    public static void register(String name, int days) {
        RESET.put(name, days);
    }

    public static boolean resets(String status) {
        return RESET.containsKey(status);
    }

    public static int getReset(String status) {
        return RESET.get(status);
    }
}

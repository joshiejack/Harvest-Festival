package uk.joshiejack.settlements.npcs.status;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class Statuses {
    private static final Statuses INSTANCE = new Statuses();
    private final Map<ResourceLocation, Object2IntMap<String>> status = Maps.newHashMap();


    private Statuses() {}
    private static Object2IntMap<String> getOrCreateMap(ResourceLocation npc) {
        if (!INSTANCE.status.containsKey(npc)) {
            INSTANCE.status.put(npc, new Object2IntOpenHashMap<>());
        }

        return INSTANCE.status.get(npc);
    }

    public static void setData(Map<ResourceLocation, Object2IntMap<String>> data) {
        INSTANCE.status.clear();
        for (Map.Entry<ResourceLocation, Object2IntMap<String>> e: data.entrySet()) {
            INSTANCE.status.put(e.getKey(), e.getValue());
        }
    }

    public static int total(String status) {
        int count = 0;
        for (ResourceLocation k: INSTANCE.status.keySet()) {
            count += INSTANCE.status.get(k).getInt(status);
        }

        return count;
    }

    public static int getValue(ResourceLocation npc, String status) {
        return getOrCreateMap(npc).getInt(status);
    }

    public static void setStatus(ResourceLocation npc, String status, int value) {
        getOrCreateMap(npc).put(status, value);
    }
}

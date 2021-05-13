package uk.joshiejack.settlements.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.world.town.Town;

import java.util.Collection;

@SideOnly(Side.CLIENT)
public class WorldMap {
    private static final Int2ObjectMap<Int2ObjectMap<TownClient>> towns = new Int2ObjectOpenHashMap<>();

    public static void setTowns(int dim, Collection<Town> towns) {
        towns.forEach(t -> WorldMap.getTownMap(dim).put(t.getID(), (TownClient) t));
    }

    private static Int2ObjectMap<TownClient> getTownMap(int dim) {
        Int2ObjectMap<TownClient> map = towns.get(dim);
        if (map == null) {
            map = new Int2ObjectOpenHashMap<>();
            towns.put(dim, map);
        }

        return map;
    }

    public static Collection<TownClient> getTowns(World world) {
        return getTownMap(world.provider.getDimension()).values();
    }

    public static TownClient getTownByID(int dimension, int id) {
        return getTownMap(dimension).get(id);
    }

    public static void addTown(int dimension, TownClient town) {
        getTownMap(dimension).put(town.getID(), town);
    }
}

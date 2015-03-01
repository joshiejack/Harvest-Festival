package joshie.harvestmoon.core.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.crops.Crop;

import com.google.gson.annotations.Expose;

public class Mappings {
    @Expose
    private HashMap<String, Integer> mappings = new HashMap();
    private HashMap<Integer, Crop> crops = new HashMap();
    private HashSet<Integer> taken;

    //Remove these, as they are not needed once the game is running
    public void clear() {
        mappings = null;
        taken = null;
    }

    public Integer getID(Crop crop) {
        String name = crop.getUnlocalizedName();
        //If taken is null, add all the saved mappings to the taken list
        if (taken == null) {
            taken = new HashSet();
            for (Integer i : mappings.values()) {
                taken.add(i);
            }
        }

        //If the matching string is in the map, return it
        Integer ret = mappings.get(name);
        if (ret != null) {
            crops.put(ret, crop);

            return ret; //If the ID was found in the mappings, return it
        }

        //Otherwise we should look for the next available ID
        //Put it in to the map and then blah!
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!taken.contains(i)) {
                mappings.put(name, i);
                taken.add(i);
                crops.put(i, crop);
                return i;
            }
        }

        return null;
    }

    public Crop getCrop(int ordinal) {
        return crops.get(ordinal);
    }
    
    public Set<Integer> getMappings() {
        return crops.keySet();
    }
}

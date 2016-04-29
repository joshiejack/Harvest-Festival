package joshie.harvest.player.tracking;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.core.util.SellStack;

import java.util.HashMap;
import java.util.HashSet;

public class TrackingData {
    HashMap<ICropData, Integer> cropTracker = new HashMap<ICropData, Integer>(); //How many of this crop has been Harvested
    HashSet<SellStack> sellTracker = new HashSet<SellStack>(); //What has been sold so far

    //TODO: Add obtain triggers for mystril tools, and blessed tools
    HashSet<SafeStack> obtained = new HashSet<SafeStack>();

    public void addAsObtained(SafeStack stack) {
        obtained.add(stack);
    }

    public boolean hasObtainedItem(SafeStack stack) {
        return obtained.contains(stack);
    }
}
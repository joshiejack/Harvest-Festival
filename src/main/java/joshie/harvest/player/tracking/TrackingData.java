package joshie.harvest.player.tracking;

import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.util.SafeStack;
import joshie.harvest.core.util.SellStack;

import java.util.HashMap;
import java.util.HashSet;

public class TrackingData {
    protected HashMap<ICropData, Integer> cropTracker = new HashMap(); //How many of this crop has been Harvested
    protected HashSet<SellStack> sellTracker = new HashSet(); //What has been sold so far

    //TODO: Add obtain triggers for mystril tools, and blessed tools
    protected HashSet<SafeStack> obtained = new HashSet();
    
    public void addAsObtained(SafeStack stack) {
        obtained.add(stack);
    }

    public boolean hasObtainedItem(SafeStack stack) {
        return obtained.contains(stack);
    }
}

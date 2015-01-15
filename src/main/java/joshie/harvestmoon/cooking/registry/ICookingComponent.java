package joshie.harvestmoon.cooking.registry;

import joshie.harvestmoon.util.SafeStack;

public interface ICookingComponent {
    public ICookingComponent addKey(SafeStack stack);
    public boolean isEquivalent(String unlocalized, ICookingComponent component);
}

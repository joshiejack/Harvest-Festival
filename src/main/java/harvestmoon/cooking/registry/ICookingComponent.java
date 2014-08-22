package harvestmoon.cooking.registry;

import harvestmoon.util.SafeStack;

public interface ICookingComponent {
    public ICookingComponent addKey(SafeStack stack);
    public boolean isEquivalent(String unlocalized, ICookingComponent component);
}

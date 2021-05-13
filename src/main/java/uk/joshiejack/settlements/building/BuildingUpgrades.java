package uk.joshiejack.settlements.building;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.PenguinLib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BuildingUpgrades {
                                //UPGRADE -> ORIGINAL
    private static final Map<Building, Building> UPGRADE_TARGETS = new HashMap<>();
    private static final Set<Building> OVERRIDES = Sets.newHashSet();

    public static void add(Building original, Building upgrade) {
        UPGRADE_TARGETS.put(upgrade, original);
        PenguinLib.logger.info("Registered " + upgrade.getLocalizedName() + " as an upgrade for " + original.getLocalizedName());
    }

    public static boolean overrides(Building building) { return OVERRIDES.contains(building); }
    public static void markOverrides(Building building) { OVERRIDES.add(building); }

    public static boolean isUpgrade(Building building) {
        return UPGRADE_TARGETS.containsKey(building);
    }

    public static Building getTargetForUpgrade(Building building) {
        return UPGRADE_TARGETS.get(building);
    }
}

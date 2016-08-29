package joshie.harvest.api.buildings;

import net.minecraft.util.ResourceLocation;

/** This is the building registry, it gets populated with all the buildings during,
 *  the init stage, so register buildings before hand and only use the other methods,
 *  after registration has been completed */
public interface IBuildingRegistry {
    /** This will register a building, it returns an IBuilding for your own convenience
     * @param resource  the resource location for your building, Harvest Festival will look in the buildings folder in your mods assets directory for buildings
     * @return the building object*/
    Building registerBuilding(ResourceLocation resource, long cost, int wood, int stone);

    /** Returns a building based on it's resource name, e.g.
     *      getBuildingFromName(new ResourceLocation("harvestfestival", "barn")) will return the barn building;
     * @param name  the name of the building
     * @return the building */
    Building getBuildingFromName(ResourceLocation name);

    /** Returns the resource for the building e.g. passing in the cafe building will return
     *     a resource containing harvestfestival:cafe
     * @param building  this is the building you want the resource name of
     * @return  the resource name */
    ResourceLocation getNameForBuilding(Building building);
}

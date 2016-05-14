package joshie.harvest.api.buildings;

import net.minecraft.entity.player.EntityPlayer;

public interface IBuilding {
    public IBuilding setSpecialRules(ISpecialPurchaseRules rules);

    String getLocalisedName();

    /** How much gold this building costs **/
    long getCost();

    /** How much wood this building costs **/
    int getWoodCount();

    /** How much stone this building costs **/
    int getStoneCount();

    /** Whether this building can be purchased or not **/
     ISpecialPurchaseRules getRules();

    boolean hasRequirements(EntityPlayer player);
    IBuildingProvider getProvider();

    int getOffsetY();

    long getTickTime();
}
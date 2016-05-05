package joshie.harvest.api.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public interface IBuilding {
    /** Sets Provider **/
    public IBuilding setProvider(IBuildingProvider provider);

    public IBuilding setSpecialRules(ISpecialPurchaseRules rules);

    /** Returns the resource **/
    public ResourceLocation getResource();

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
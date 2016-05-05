package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingProvider;
import joshie.harvest.api.buildings.ISpecialPurchaseRules;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class BuildingAbstract implements IBuilding {
    private transient ISpecialPurchaseRules special = new SpecialRulesDefault();
    private transient IBuildingProvider provider;
    private transient String toLocalise;
    //List of all placeable elements
    public transient ResourceLocation resource;
    public ResourceLocation[] requirements;
    public long cost;
    public int wood;
    public int stone;
    public int offsetY;
    public long tickTime;
    public Placeable[] components; //Set to null after loading

    public BuildingAbstract(){}
    public BuildingAbstract(String string) {
        resource = new ResourceLocation("harvestfestival", string);
    }

    @Override
    public String getLocalisedName() {
        return I18n.translateToLocal(toLocalise);
    }

    @Override
    public ResourceLocation getResource() {
        return resource;
    }

    public boolean canBuy(World world, EntityPlayer player) {
        return false;
    }

    @Override
    public IBuilding setProvider(IBuildingProvider provider) {
        this.provider = provider;
        this.provider.setBuilding(this);
        this.toLocalise = resource.getResourceDomain().toLowerCase() + ".structures." + resource.getResourcePath().toLowerCase();
        return this;
    }

    @Override
    public IBuildingProvider getProvider() {
        return provider;
    }

    @Override
    public long getTickTime() {
        return tickTime;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    @Override
    public IBuilding setSpecialRules(ISpecialPurchaseRules special) {
        this.special = special;
        return this;
    }

    @Override
    public ISpecialPurchaseRules getRules() {
        return special;
    }

    @Override
    public boolean hasRequirements(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getTown().hasBuildings(requirements);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public int getWoodCount() {
        return wood;
    }

    @Override
    public int getStoneCount() {
        return stone;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BuildingAbstract && resource != null && resource.equals(((BuildingAbstract) o).resource);
    }

    @Override
    public int hashCode() {
        return resource == null? 0 : resource.hashCode();
    }

    public String[] getRequirements() {
        return new String[] { "miningHill", "miningHut", "goddessPond" };
    }

    public IBuilding addBlocks(List<Placeable> list) {
        return this;
    }
}

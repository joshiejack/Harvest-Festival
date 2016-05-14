package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingProvider;
import joshie.harvest.api.buildings.ISpecialPurchaseRules;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.core.helpers.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class Building extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<Building> implements IBuilding {
    private transient ISpecialPurchaseRules special = new SpecialRulesDefault();
    private transient BuildingProvider provider;
    private transient String toLocalise;
    public ResourceLocation[] requirements;
    public long cost;
    public int wood;
    public int stone;
    public int offsetY;
    public long tickTime;
    public Placeable[] components; //Set to null after loading

    public Building(){}

    @Override
    public String getLocalisedName() {
        return I18n.translateToLocal(toLocalise);
    }

    public Building setProvider(BuildingProvider provider) {
        this.provider = provider;
        this.provider.setBuilding(this);
        if (getRegistryName() != null) {
            this.toLocalise = getRegistryName().getResourceDomain().toString().toLowerCase() + ".structures." + getRegistryName().getResourcePath().toLowerCase();
        }

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
        return TownHelper.getClosestTownToPlayer(player).hasBuildings(requirements);
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
        return o instanceof Building && getRegistryName() != null && getRegistryName().equals(((Building) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }
}

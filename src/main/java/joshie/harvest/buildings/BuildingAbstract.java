package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class BuildingAbstract implements IBuilding {
    private IBuildingProvider provider;
    //List of all placeable elements
    private String name;

    public BuildingAbstract(String string) {
        this.name = string;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProvider(IBuildingProvider provider) {
        this.provider = provider;
    }

    @Override
    public IBuildingProvider getProvider() {
        return provider;
    }

    @Override
    public long getTickTime() {
        return 20;
    }

    @Override
    public int getOffsetY() {
        return 0;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return false;
    }

    @Override
    public long getCost() {
        return 0;
    }

    @Override
    public int getWoodCount() {
        return 0;
    }

    @Override
    public int getStoneCount() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof String) && name.equals(o);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

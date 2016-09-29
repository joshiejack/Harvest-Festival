package joshie.harvest.town;

import joshie.harvest.buildings.BuildingStage;

import java.util.LinkedList;

public class TownDataClient extends TownData {
    public void setBuilding(LinkedList<BuildingStage> building) {
        this.building = building;
    }
}
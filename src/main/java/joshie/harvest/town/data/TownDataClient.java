package joshie.harvest.town.data;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.quests.data.QuestDataClient;

import java.util.LinkedList;

public class TownDataClient extends TownData<QuestDataClient> {
    private final QuestDataClient quest = new QuestDataClient();

    @Override
    public QuestDataClient getQuests() {
        return quest;
    }

    public void addBuilding(TownBuilding building) {
        buildings.put(Building.REGISTRY.getKey(building.building), building);
        inhabitants.addAll(building.building.getInhabitants());
    }

    public void setBuilding(LinkedList<BuildingStage> building) {
        this.building = building;
    }

    public void setDailyQuest(Quest dailyQuest) {
        this.dailyQuest = dailyQuest;
    }
}
package joshie.harvest.town.data;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.knowledge.letter.LetterDataClient;
import joshie.harvest.quests.data.QuestDataClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;

@SideOnly(Side.CLIENT)
public class TownDataClient extends TownData<QuestDataClient, LetterDataClient> {
    private final QuestDataClient quest = new QuestDataClient();
    private final LetterDataClient letters = new LetterDataClient();

    @Override
    public QuestDataClient getQuests() {
        return quest;
    }

    @Override
    public LetterDataClient getLetters() {
        return letters;
    }

    public void addBuilding(TownBuilding building) {
        buildings.put(Building.REGISTRY.getKey(building.building), building);
        inhabitants.addAll(building.building.getInhabitants());
    }

    public void setBuilding(LinkedList<BuildingStage> buildingQueue) {
        this.buildingQueue = buildingQueue;
    }

    public void setCentre(BlockPos centre) {
        this.townCentre = centre;
    }

    public void setDailyQuest(Quest dailyQuest) {
        this.dailyQuest = dailyQuest;
    }

    public void setFestival(Festival festival, int days) {
        this.festival = festival;
        this.festivalDays = days;
    }
}
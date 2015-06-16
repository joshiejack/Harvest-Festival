package joshie.harvest.player;

import java.util.UUID;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.quests.QuestStats;
import joshie.harvest.quests.QuestStatsClient;
import joshie.harvest.relations.RelationTrackerClient;
import joshie.harvest.relations.RelationshipTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerTrackerClient extends PlayerTracker {
    //Questing
    private PlayerStats stats = new PlayerStats();
    private QuestStatsClient quests = new QuestStatsClient();
    private RelationTrackerClient relationships = new RelationTrackerClient();
    private EntityNPCBuilder builder;
    private FridgeContents fridge;

    @Override
    public EntityPlayer getAndCreatePlayer() {
        return MCClientHelper.getPlayer();
    }

    @Override
    public RelationshipTracker getRelationships() {
        return relationships;
    }

    public FridgeContents getFridge() {
        return fridge;
    }

    public void setFridge(FridgeContents fridge) {
        this.fridge = fridge;
    }

    @Override
    public QuestStats getQuests() {
        return quests;
    }

    public long getGold() {
        return playerStats.getGold();
    }

    public boolean addGold(long amount) {
        playerStats.addGold(amount);
        return true;
    }

    public void setGold(long gold) {
        playerStats.setGold(gold);
    }

    public double getStamina() {
        return playerStats.getStamina();
    }

    public double getFatigue() {
        return playerStats.getFatigue();
    }

    public void affectStats(double stamina, double fatigue) {
        playerStats.affectStats(stamina, fatigue);
    }

    public void setStats(double stamina, double fatigue, double staminaMax, double fatigueMin) {
        playerStats.setStats(stamina, fatigue, staminaMax, fatigueMin);
    }

    public void setBirthday(int day, Season season, int year) {
        playerStats.setBirthday(HFApi.CALENDAR.newDate(day, season, year));
    }

    public ICalendarDate getBirthday() {
        return playerStats.getBirthday();
    }

    @Override
    public EntityNPCBuilder getBuilder(World world) {
        if (builder != null) return builder;
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityNPCBuilder) {
                UUID owner = ((EntityNPCBuilder) entity).owning_player;
                if (owner == UUIDHelper.getPlayerUUID(MCClientHelper.getPlayer())) {
                    builder = (EntityNPCBuilder) entity;
                    return builder;
                }
            }
        }

        return null;
    }
}

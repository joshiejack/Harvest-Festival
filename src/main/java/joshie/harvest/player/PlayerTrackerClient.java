package joshie.harvest.player;

import java.util.UUID;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.relations.RelationTrackerClient;
import joshie.harvest.relations.RelationshipTracker;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerTrackerClient extends PlayerTracker {
    //Questing
    private ICalendarDate birthday = HFApi.CALENDAR.newDate(0, null, 0);
    private QuestsClientside quests = new QuestsClientside();
    private RelationTrackerClient relationships = new RelationTrackerClient();
    private EntityNPCBuilder builder;
    private FridgeContents fridge;
    private double staminaMax = 100D;
    private double fatigueMin = 0D;
    private double stamina;
    private double fatigue;
    private long gold;

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

    public QuestsClientside getQuests() {
        return quests;
    }

    //Returns the name of your current lover
    public String getLover() {
        return Translate.translate("nolover");
    }

    public long getGold() {
        return gold;
    }

    public boolean addGold(long amount) {
        gold += amount;
        return true;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public double getStamina() {
        return this.stamina;
    }

    public double getFatigue() {
        return this.fatigue;
    }

    public void affectStats(double stamina, double fatigue) {
        this.stamina += stamina;
        this.fatigue += fatigue;

        if (this.stamina >= staminaMax) {
            this.stamina = staminaMax;
        }

        if (this.fatigue <= fatigueMin) {
            this.fatigue = fatigueMin;
        }
    }

    public void setStats(double stamina, double fatigue, double staminaMax, double fatigueMin) {
        this.stamina = stamina;
        this.fatigue = fatigue;
        this.staminaMax = staminaMax;
        this.fatigueMin = fatigueMin;
    }

    public void setBirthday(int day, Season season, int year) {
        this.birthday = HFApi.CALENDAR.newDate(day, season, year);
    }

    public ICalendarDate getBirthday() {
        return birthday;
    }

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

package joshie.harvestmoon.player;

import java.util.HashMap;
import java.util.UUID;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.util.Translate;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.NPC;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerDataClient {
    //Questing
    private CalendarDate birthday = new CalendarDate();
    private QuestsClientside quests = new QuestsClientside();
    private HashMap<UUID, Short> entity_relations = new HashMap();
    private HashMap<NPC, Short> npc_relations = new HashMap();
    private FridgeContents fridge;
    private double staminaMax = 100D;
    private double fatigueMin = 0D;
    private double stamina;
    private double fatigue;
    private long gold;

    public FridgeContents getFridge() {
        return fridge;
    }

    public void setFridge(FridgeContents fridge) {
        this.fridge = fridge;
    }
    
    public QuestsClientside getQuests() {
        return quests;
    }

    public int getRelationship(EntityLivingBase living) {
        if (living instanceof EntityNPC) return getRelationship(((EntityNPC) living).getNPC());
        Short ret = entity_relations.get(living.getPersistentID());
        return ret == null ? 0 : ret;
    }

    public int getRelationship(NPC npc) {
        Short ret = npc_relations.get(npc);
        return ret == null ? 0 : ret;
    }

    public void setRelationship(UUID id, int value) {
        entity_relations.put(id, (short) value);
    }

    public void setRelationship(NPC npc, int value) {
        npc_relations.put(npc, (short) value);
    }

    public boolean removeRelations(EntityLivingBase living) {
        if (living instanceof EntityNPC) return removeRelations(((EntityNPC) living).getNPC());
        entity_relations.remove(living.getPersistentID());
        return true;
    }

    public boolean removeRelations(NPC npc) {
        npc_relations.remove(npc);
        return true;
    }

    public boolean canMarry() {
        for (NPC npc: npc_relations.keySet()) {
            int value = npc_relations.get(npc);
            if (value >= joshie.harvestmoon.core.config.NPC.MARRIAGE_REQUIREMENT) {
                return true;
            }
        }
        
        return false;
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
        this.birthday = new CalendarDate(day, season, year);
    }

    public CalendarDate getBirthday() {
        return birthday;
    }
}

package joshie.harvestmoon.player;

import java.util.HashMap;
import java.util.UUID;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.util.Translate;
import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerDataClient {
    //Questing
    private QuestsClientside quests = new QuestsClientside();
    private HashMap<UUID, Short> entity_relations = new HashMap();
    private HashMap<NPC, Short> npc_relations = new HashMap();
    private double stamina;
    private double fatigue;
    private int gold;

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

    //Returns the name of your current lover
    public String getLover() {
        return Translate.translate("nolover");
    }

    public int getGold() {
        return gold;
    }

    public boolean addGold(int amount) {
        gold += amount;
        return true;
    }

    public void setGold(int gold) {
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
    }

    public void setStats(double stamina, double fatigue) {
        this.stamina = stamina;
        this.fatigue = fatigue;
    }
}

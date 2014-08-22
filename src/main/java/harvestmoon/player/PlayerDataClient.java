package harvestmoon.player;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerDataClient {
    private static HashMap<UUID, Short> relationships = new HashMap();
    private static double stamina;
    private static double fatigue;
    private static int gold;

    @SideOnly(Side.CLIENT)
    public int getRelationship(EntityLivingBase living) {
        Short ret = relationships.get(living.getPersistentID());
        return ret == null ? 0 : ret;
    }

    @SideOnly(Side.CLIENT)
    public void setRelationship(UUID id, int value) {
        relationships.put(id, (short) value);
    }

    @SideOnly(Side.CLIENT)
    public void removeRelations(EntityLivingBase entity) {
        relationships.remove(entity.getPersistentID());
    }

    @SideOnly(Side.CLIENT)
    public int getGold() {
        return gold;
    }

    @SideOnly(Side.CLIENT)
    public boolean addGold(int amount) {
        gold += amount;
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setGold(int gold) {
        this.gold = gold;
    }

    @SideOnly(Side.CLIENT)
    public double getStamina() {
        return this.stamina;
    }

    @SideOnly(Side.CLIENT)
    public double getFatigue() {
        return this.fatigue;
    }

    @SideOnly(Side.CLIENT)
    public void affectStats(double stamina, double fatigue) {
        this.stamina += stamina;
        this.fatigue += fatigue;
    }

    @SideOnly(Side.CLIENT)
    public void setStats(double stamina, double fatigue) {
        this.stamina = stamina;
        this.fatigue = fatigue;
    }
}

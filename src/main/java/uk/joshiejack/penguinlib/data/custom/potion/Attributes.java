package uk.joshiejack.penguinlib.data.custom.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.UUID;

public class Attributes {
    public String name;
    public double amount;
    public int operation;

    @Nullable
    public IAttribute get() {
        switch (name) {
            case "max_health": return SharedMonsterAttributes.MAX_HEALTH;
            case "follow_range": return SharedMonsterAttributes.FOLLOW_RANGE;
            case "knockback_resistance": return SharedMonsterAttributes.KNOCKBACK_RESISTANCE;
            case "movement_speed": return SharedMonsterAttributes.MOVEMENT_SPEED;
            case "flying_speed": return SharedMonsterAttributes.FLYING_SPEED;
            case "attack_damage": return SharedMonsterAttributes.ATTACK_DAMAGE;
            case "attack_speed": return SharedMonsterAttributes.ATTACK_SPEED;
            case "armor": return SharedMonsterAttributes.ARMOR;
            case "armor_toughness": return SharedMonsterAttributes.ARMOR_TOUGHNESS;
            case "luck": return SharedMonsterAttributes.LUCK;
            default:    return null;
        }
    }

    public UUID getUUID(ResourceLocation registry) {
        return UUID.nameUUIDFromBytes((name + registry.toString()).getBytes());
    }
}

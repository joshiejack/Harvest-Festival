package joshie.harvest.cooking.item;

import com.google.common.collect.Multimap;
import joshie.harvest.cooking.item.ItemUtensil.Utensil;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

import static joshie.harvest.cooking.item.ItemUtensil.Utensil.KNIFE;

public class ItemUtensil extends ItemHFEnum<ItemUtensil, Utensil> {
    public enum Utensil implements IStringSerializable {
        BLADE, KNIFE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemUtensil() {
        super(HFTab.COOKING, Utensil.class);
    }

    @Override
    public boolean shouldDisplayInCreative(Utensil utensil) {
        return utensil == KNIFE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (getEnumFromStack(stack) != KNIFE) {
            return super.getAttributeModifiers(slot, stack);
        } else {
            Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
            if (slot == EntityEquipmentSlot.MAINHAND) {
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 2.0D, 0));
                multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -1D, 0));
            }

            return multimap;
        }
    }
}

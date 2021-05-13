package uk.joshiejack.penguinlib.item.custom;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.data.custom.potion.Attributes;
import uk.joshiejack.penguinlib.item.base.ItemArmorBase;
import uk.joshiejack.penguinlib.scripting.Scripting;

import javax.annotation.Nonnull;

public class ItemCustomArmor extends ItemArmorBase {
    private final ResourceLocation script;

    private final Attributes[] attributes;
    public ItemCustomArmor(ResourceLocation registry, ResourceLocation script, ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, Attributes... attributes) {
        super(registry, materialIn, equipmentSlotIn);
        this.script = script;
        this.attributes = attributes;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (itemSlot == armorType.getSlotIndex() && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(@Nonnull EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.armorType) {
            for (Attributes attribute : attributes) {
                IAttribute attribute1 = attribute.get();
                if (attribute1 != null) {
                    multimap.put(attribute1.getName(), new AttributeModifier(attribute.getUUID(getRegistryName()), "Armor modifier", attribute.amount, attribute.operation));
                }
            }
        }

        return multimap;
    }
}

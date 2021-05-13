package uk.joshiejack.penguinlib.data.custom.item;

import uk.joshiejack.penguinlib.data.custom.potion.Attributes;
import uk.joshiejack.penguinlib.item.custom.ItemCustomArmor;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:armor")
public class CustomItemArmorData extends CustomItemTieredTool<ItemCustomArmor, CustomItemArmorData> {
    public Attributes[] attributes;
    public String armorMaterial;
    public String equipmentSlot;

    @Nonnull
    @Override
    public ItemCustomArmor build(ResourceLocation registryName, @Nonnull CustomItemArmorData main, @Nullable CustomItemArmorData... sub) {
        return new ItemCustomArmor(registryName, main.getScript(), CustomItemArmorData.fromString(main.armorMaterial), EntityEquipmentSlot.fromString(main.equipmentSlot), main.attributes);
    }

    public static ItemArmor.ArmorMaterial fromString(String targetName) {
        for (ItemArmor.ArmorMaterial material : ItemArmor.ArmorMaterial.values()) {
            if (material.name.equals(targetName)) {
                return material;
            }
        }

        throw new IllegalArgumentException("Invalid material '" + targetName + "'");
    }
}

package uk.joshiejack.penguinlib.data.custom.material;

import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("armor_material:standard")
public class CustomArmorMaterialData extends AbstractCustomData<ItemArmor.ArmorMaterial, CustomArmorMaterialData> {
    private String texture;
    private int[] reductions;
    private int durability;
    private int enchantability;
    private ResourceLocation sound;
    private float toughness;

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public ItemArmor.ArmorMaterial build(ResourceLocation registryName, @Nonnull CustomArmorMaterialData main, @Nullable CustomArmorMaterialData... data) {
        return EnumHelper.addArmorMaterial(registryName.getPath(), main.texture, main.durability, main.reductions, main.enchantability, SoundEvent.REGISTRY.getObject(main.sound), main.toughness);
    }
}

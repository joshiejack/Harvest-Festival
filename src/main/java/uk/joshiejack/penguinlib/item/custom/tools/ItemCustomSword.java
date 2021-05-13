package uk.joshiejack.penguinlib.item.custom.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemSwordData;
import uk.joshiejack.penguinlib.item.base.ItemBaseSword;
import uk.joshiejack.penguinlib.scripting.Scripting;

import javax.annotation.Nonnull;

public class ItemCustomSword extends ItemBaseSword {
    private final ResourceLocation script;

    public ItemCustomSword(ResourceLocation registryName, CustomItemSwordData data) {
        super(registryName, data.getToolMaterial().damage);
        this.script = data.getScript();
        this.setMaxDamage(data.getToolMaterial().maxUses);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }
}

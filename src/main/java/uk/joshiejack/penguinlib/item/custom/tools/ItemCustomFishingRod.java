package uk.joshiejack.penguinlib.item.custom.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.item.tools.CustomItemFishingRodData;
import uk.joshiejack.penguinlib.item.base.ItemBaseFishingRod;
import uk.joshiejack.penguinlib.scripting.Scripting;

import javax.annotation.Nonnull;

public class ItemCustomFishingRod extends ItemBaseFishingRod {
    private final ResourceLocation script;

    public ItemCustomFishingRod(ResourceLocation registryName, CustomItemFishingRodData data) {
        super(registryName, data.getToolMaterial().harvestLevel);
        this.setMaxDamage(data.getToolMaterial().maxUses);
        this.script = data.getScript();
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (isSelected && entity instanceof EntityPlayer) {
            Scripting.callFunction(script, "onItemSelected", entity);
        }
    }
}

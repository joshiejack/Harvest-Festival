package joshie.harvest.crops.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.crops.HFCrops;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCrop extends ItemHFFML<ItemCrop, Crop> implements IShippable, ICropProvider {
    public ItemCrop() {
        super(Crop.REGISTRY);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        Crop crop = getObjectFromStack(stack);
        if (crop == HFCrops.GRASS) return 0;
        else {
            return crop.getSellValue(stack);
        }
    }

    @Override
    public Crop getCrop(ItemStack stack) {
        return getObjectFromStack(stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            Crop crop = getCrop(stack);
            player.getFoodStats().addStats(crop.getHunger(), crop.getSaturation());
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(false)) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName(true);
    }

    @Override
    public boolean shouldDisplayInCreative(Crop crop) {
        return crop != Crop.NULL_CROP && crop.getCropStack(1).getItem() == this;
    }

    @Override
    public Crop getNullValue() {
        return Crop.NULL_CROP;
    }
}
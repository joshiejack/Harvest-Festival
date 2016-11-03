package joshie.harvest.fishing.item;

import joshie.harvest.api.fishing.IWeightedItem;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.fishing.item.ItemFish.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;

import static joshie.harvest.fishing.item.ItemFish.*;

public class ItemFishingRod extends ItemTool<ItemFishingRod> implements IWeightedItem {
    public ItemFishingRod() {
        super("fishing_rod", new HashSet<>());
        setCreativeTab(HFTab.FISHING);
        addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn == null ? 0.0F : (entityIn.getHeldItemMainhand() == stack && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null ? 1.0F : 0.0F);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack getInWeightRange(Random rand, ItemStack held, ItemStack stack) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        Fish fish = HFFishing.FISH.getEnumFromStack(stack);
        int min = 1, max = 1;
        switch (getTier(held)) {
            case BASIC:
                min = SMALL_FISH;
                max = SMALL_FISH;
                break;
            case COPPER:
                min = SMALL_FISH;
                max = MEDIUM_FISH;
                break;
            case SILVER:
                min = SMALL_FISH;
                max = LARGE_FISH;
                break;
            case GOLD:
                min = SMALL_FISH;
                max = GIANT_FISH;
                break;
            case MYSTRIL:
                min = MEDIUM_FISH;
                max = GIANT_FISH;
                break;
            case CURSED:
            case BLESSED:
                min = LARGE_FISH;
                max = GIANT_FISH;
                break;
            case MYTHIC:
                min = GIANT_FISH;
                max = GIANT_FISH;
                break;
        }

        int size;
        if (min == max) size = min;
        else {
            size = min + rand.nextInt(1 + (max - min));
        }

        stack.getTagCompound().setDouble(SIZE, fish.getLengthFromSizeOfFish(size));
        return stack;
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, @Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (player.fishEntity != null) {
            int i = player.fishEntity.handleHookRetraction();
            stack.damageItem(i, player);
            player.swingArm(hand);
        } else {
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityFishHookHF(world, player));
            }

            player.swingArm(hand);
            player.addStat(StatList.getObjectUseStats(this));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}

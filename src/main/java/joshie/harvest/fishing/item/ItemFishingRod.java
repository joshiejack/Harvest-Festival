package joshie.harvest.fishing.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;

import static joshie.harvest.fishing.item.ItemFish.*;

public class ItemFishingRod extends ItemTool<ItemFishingRod> {
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
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, @Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (player.fishEntity != null) {
            player.fishEntity.handleHookRetraction();
            ToolHelper.consumeHunger(player, getExhaustionRate(stack));
            stack.getSubCompound("Data", true).setInteger("Damage", getDamageForDisplay(stack) + 1);
            player.swingArm(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else if (canUse(stack)) {
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityFishHookHF(world, player));
            }

            player.swingArm(hand);
            player.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    public int getMinimumFishSize(ItemStack held) {
        switch (getTier(held)) {
            case BASIC:
            case COPPER:
            case SILVER:
            case GOLD:
                return SMALL_FISH;
            case MYSTRIL:
                return MEDIUM_FISH;
            case CURSED:
            case BLESSED:
                return LARGE_FISH;
            case MYTHIC:
                return GIANT_FISH;
            default:
                return SMALL_FISH;
        }
    }

    public int getMaximumFishSize(ItemStack held) {
        switch (getTier(held)) {
            case BASIC:
                return SMALL_FISH;
            case COPPER:
                return MEDIUM_FISH;
            case SILVER:
                return LARGE_FISH;
            case GOLD:
            case MYSTRIL:
            case CURSED:
            case BLESSED:
            case MYTHIC:
                return GIANT_FISH;
            default:
                return SMALL_FISH;
        }
    }
}

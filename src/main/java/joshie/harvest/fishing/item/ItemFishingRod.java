package joshie.harvest.fishing.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.tools.ToolHelper;
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

    public int getBaitAmount(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger("Bait") : 0;
    }

    boolean addBait(ItemStack rod, ItemStack bait) {
        NBTTagCompound tag = rod.hasTagCompound() ? rod.getTagCompound() : new NBTTagCompound();
        int existing = tag.getInteger("Bait");
        if (existing + bait.stackSize > 999) return false;
        tag.setInteger("Bait", existing + bait.stackSize);
        rod.setTagCompound(tag); //Reset the tag
        return true;
    }

    private void removeBait(EntityPlayer player, ItemStack rod, int amount, boolean returning) {
        NBTTagCompound tag = rod.hasTagCompound() ? rod.getTagCompound() : new NBTTagCompound();
        int existing = tag.getInteger("Bait");
        int newValue = existing - amount;
        if (newValue < 0) tag.removeTag("Bait");
        else tag.setInteger("Bait", newValue);
        rod.setTagCompound(tag); //Reset the tag
        if (returning && existing > 0) {
            ItemStack bait = HFFishing.JUNK.getStackFromEnum(Junk.BAIT, newValue < 0 ? amount + newValue : amount);
            SpawnItemHelper.addToPlayerInventory(player, bait);
        }
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
            if (player.fishEntity.handleHookRetraction() != 0) removeBait(player, stack, 1, false);
            ToolHelper.consumeHunger(player, getExhaustionRate(stack));
            stack.getSubCompound("Data", true).setInteger("Damage", getDamageForDisplay(stack) + 1);
            player.swingArm(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else if (canUse(stack)) {
            if (player.isSneaking()) {
                removeBait(player, stack, 64, true);
            } else {
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityFishHookHF(world, player, getTier(stack).getToolLevel(), getBaitAmount(stack)));
                }

                player.swingArm(hand);
                player.addStat(StatList.getObjectUseStats(this));
            }
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

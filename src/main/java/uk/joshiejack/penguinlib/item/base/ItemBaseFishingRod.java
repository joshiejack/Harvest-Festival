package uk.joshiejack.penguinlib.item.base;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import uk.joshiejack.penguinlib.data.database.registries.BaitRegistry;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;

public abstract class ItemBaseFishingRod extends ItemFishingRod implements IPenguinItem {
    private final String prefix;
    private final int quality;

    public ItemBaseFishingRod(ResourceLocation registry, int quality) {
        this.quality = quality;
        prefix = registry.getNamespace() + ".item." + registry.getPath();
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    public int getQuality() {
        return quality;
    }

    @SuppressWarnings("ConstantConditions")
    public static int getBaitAmount(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger("Bait") : 0;
    }

    @SuppressWarnings("ConstantConditions")
    public static ItemStack getBaitStack(ItemStack stack) {
        return stack.hasTagCompound() ? new ItemStack(stack.getTagCompound().getCompoundTag("Stack")) : ItemStack.EMPTY;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean addBait(ItemStack rod, ItemStack bait) {
        NBTTagCompound tag = rod.hasTagCompound() ? rod.getTagCompound() : new NBTTagCompound();
        ItemStack stack = new ItemStack(tag);
        if (!stack.isEmpty() && !ItemStack.areItemsEqual(stack, bait)) return false;
        int existing = tag.getInteger("Bait");
        if (existing + bait.getCount() > 999) return false;
        else {
            tag.setTag("Stack", bait.writeToNBT(new NBTTagCompound()));
            tag.setInteger("Bait", existing + bait.getCount());
            rod.setTagCompound(tag); //Reset the tag
            return true;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void removeBait(EntityPlayer player, ItemStack rod, int amount, boolean returning) {
        NBTTagCompound tag = rod.hasTagCompound() ? rod.getTagCompound() : new NBTTagCompound();
        int existing = tag.getInteger("Bait");
        int newValue = existing - amount;
        if (newValue < 0) tag.removeTag("Bait");
        else tag.setInteger("Bait", newValue);

        rod.setTagCompound(tag); //Reset the tag
        if (returning && existing > 0) {
            ItemStack ret = new ItemStack(tag.getCompoundTag("Stack"));
            if (newValue < 0) ret.setCount(amount + newValue);
            else ret.setCount(amount);
            ItemHandlerHelper.giveItemToPlayer(player, ret);
        }
    }

    private boolean canUse(ItemStack stack) {
        return stack.getItemDamage() < stack.getMaxDamage() - 1 && getBaitAmount(stack) > 0;
    }


    protected int getBaitSpeedBoost(ItemStack stack) {
        return BaitRegistry.SPEED.getValue(stack);
    }

    protected int getBaitLuckBoost(ItemStack stack) {
        return BaitRegistry.LUCK.getValue(stack);
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.fishEntity != null)
        {
            int i = player.fishEntity.handleHookRetraction();
            if (i != 0) {
                removeBait(player, stack, 1, false);
            }

            stack.damageItem(i, player);
            player.swingArm(hand);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else if (canUse(stack)) {
            if (player.isSneaking()) {
                removeBait(player, stack, 64, true);
            } else {
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    EntityFishHook entityfishhook = new EntityFishHook(world, player);
                    int amount = getBaitAmount(stack);
                    ItemStack bait = getBaitStack(stack);
                    int j = EnchantmentHelper.getFishingSpeedBonus(stack) + quality * (1 + (amount > 0 ? getBaitSpeedBoost(bait): 0)); //Default speed greater
                    if (j > 0) {
                        entityfishhook.setLureSpeed(Math.min(5, j));
                    }


                    int k = EnchantmentHelper.getFishingLuckBonus(stack) + quality + getBaitLuckBoost(bait);
                    if (k > 0) {
                        entityfishhook.setLuck(k);
                    }

                    world.spawnEntity(entityfishhook);
                }

                player.swingArm(hand);
                player.addStat(StatList.getObjectUseStats(this));
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}

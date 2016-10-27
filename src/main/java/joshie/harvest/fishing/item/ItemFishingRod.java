package joshie.harvest.fishing.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.fishing.entity.EntityFishHookHF;
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

import static net.minecraft.world.storage.loot.LootTableList.GAMEPLAY_FISHING;

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

    public ResourceLocation getLootTable(ItemStack stack) {
        if (stack == null) return GAMEPLAY_FISHING;
        else {
            switch (getTier(stack)) {
                case BASIC:
                    return LootStrings.FISHING_BASIC;
                case COPPER:
                    return LootStrings.FISHING_COPPER;
                case SILVER:
                    return LootStrings.FISHING_SILVER;
                case GOLD:
                    return LootStrings.FISHING_GOLD;
                case MYSTRIL:
                    return LootStrings.FISHING_MYSTRIL;
                case CURSED:
                case BLESSED:
                    return LootStrings.FISHING_CURSED;
                case MYTHIC:
                    return LootStrings.FISHING_MYTHIC;
                default:
                    return GAMEPLAY_FISHING;
            }
        }
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

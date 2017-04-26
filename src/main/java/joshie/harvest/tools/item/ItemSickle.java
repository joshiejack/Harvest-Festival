package joshie.harvest.tools.item;

import com.google.common.collect.Multimap;
import joshie.harvest.core.base.item.ItemToolChargeable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ItemSickle extends ItemToolChargeable<ItemSickle> {
    public ItemSickle() {
        super("sickle", new HashSet<>());
    }

    @Override
    public int getFront(ToolTier tier) {
        switch (tier) {
            case BASIC:
            case COPPER:
                return 0;
            case SILVER:
                return 1;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 4;
            case CURSED:
            case BLESSED:
                return 8;
            case MYTHIC:
                return 14;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ToolTier tier) {
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
            case SILVER:
            case GOLD:
                return 1;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 4;
            case MYTHIC:
                return 7;
            default:
                return 0;
        }
    }

    @Override
    public boolean canLevel(@Nonnull ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state))
                return true;
        }

        return state.getBlock() instanceof IPlantable;
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World worldIn, IBlockState state, BlockPos startPos, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer)entityLiving);
            EnumFacing front = EntityHelper.getFacingFromEntity(player);
            ToolTier tier = getChargeTier(getCharge(stack));

            //Facing North, We Want East and West to be 1, left * this.left
            for (int x2 = getXMinus(tier, front, startPos.getX()); x2 <= getXPlus(tier, front, startPos.getX()); x2++) {
                for (int z2 = getZMinus(tier, front, startPos.getZ()); z2 <= getZPlus(tier, front, startPos.getZ()); z2++) {
                    if (canUse(stack) && canBeDamaged()) {
                        BlockPos newPos = new BlockPos(x2, startPos.getY(), z2);
                        state = worldIn.getBlockState(newPos);
                        if (canLevel(stack, state)) { //Break the block and collect the drops
                            ToolHelper.performTask(player, stack, this);
                            if (!newPos.equals(startPos)) {
                                if (!worldIn.isRemote) {
                                    if (state.getBlock().canHarvestBlock(worldIn, newPos, player)) {
                                        boolean flag = state.getBlock().removedByPlayer(state, worldIn, newPos, player, true);
                                        if (flag) {
                                            state.getBlock().onBlockDestroyedByPlayer(worldIn, newPos, state);
                                            state.getBlock().harvestBlock(worldIn, player, newPos, state, worldIn.getTileEntity(newPos), stack);
                                        }
                                    }
                                }
                            }
                        }

                        stack.getOrCreateSubCompound("Data").setInteger("Damage", getDamageForDisplay(stack) + 1);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public float getStrVsBlock(@Nonnull ItemStack stack, IBlockState state) {
        if (canUse(stack)) {
            Material material = state.getMaterial();
            return (state.getBlock() != Blocks.GRASS && material == Material.GRASS) || material == Material.LEAVES || material == Material.VINE ? 10F : super.getStrVsBlock(stack, state);
        } else return 0.05F;
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        ToolTier tier = getTier(stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 5.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 3D + (tier.getToolLevel() - 6.0D), 0));
        }

        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        super.addInformation(stack, player, list, flag);
        int charge = getCharge(stack);
        ToolTier thisTier = getTier(stack);
        if (thisTier != ToolTier.BASIC) {
            ToolTier tier = LEVEL_TO_TIER.get(charge);
            list.add(TextFormatting.GOLD + TextHelper.translate("sickle.tooltip.charge." + tier.name().toLowerCase(Locale.ENGLISH)));
            list.add("-------");
            if (charge < thisTier.getToolLevel())
                list.add(TextFormatting.AQUA + "" + TextFormatting.ITALIC + TextHelper.translate("sickle.tooltip.charge"));
            if (charge != 0)
                list.add(TextFormatting.RED + "" + TextFormatting.ITALIC + TextHelper.translate("sickle.tooltip.discharge"));
        }
    }

    @Override
    protected String getLevelName(@Nonnull ItemStack stack, int charges) {
        int maximum = getMaxCharge(stack);
        int charge = getCharge(stack);
        int newCharge = Math.min(maximum, charge + charges);
        return charge == newCharge ? null : TextHelper.translate("sickle.tooltip.charge." + LEVEL_TO_TIER.get(newCharge).name().toLowerCase(Locale.ENGLISH));
    }
}
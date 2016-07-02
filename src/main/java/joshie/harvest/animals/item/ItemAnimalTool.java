package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IMilkable;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.ItemHFEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.*;
import static net.minecraft.util.text.TextFormatting.AQUA;

public class ItemAnimalTool extends ItemHFEnum<ItemAnimalTool, Tool> implements ICreativeSorted {
    public enum Tool implements IStringSerializable {
        MILKER, BRUSH, MEDICINE, CHICKEN_FEED, MIRACLE_POTION;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemAnimalTool() {
        super(Tool.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getEnumFromStack(stack) == MILKER ? 32 : 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return getEnumFromStack(stack) == MILKER ? EnumAction.BOW : EnumAction.NONE;
    }

    private final HashMap<EntityPlayer, IMilkable> milkables = new HashMap<>();

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && getEnumFromStack(stack) == MILKER) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            IMilkable milkable = milkables.get(player);
            if (milkable != null) {
                milkable.milk(player);
            }
        }

        return stack;
    }

    private boolean milk(EntityPlayer player, EnumHand hand, IMilkable milkable) {
        if (milkable.canMilk()) {
            milkables.put(player, milkable);
            player.setActiveHand(hand);
            return true;
        }

        return false;
    }

    private boolean impregnate(EntityPlayer player, EntityAnimal animal, IAnimalData data, ItemStack stack) {
        if (!animal.worldObj.isRemote) {
            if (data.impregnate(player)) {
                stack.stackSize--;
            }
        }

        return true;
    }

    private boolean heal(EntityPlayer player, IAnimalData data, ItemStack stack) {
        if (!player.worldObj.isRemote) {
            if (data.heal(player)) {
                stack.stackSize--;
            }
        }

        return true;
    }

    private boolean clean(EntityPlayer player, EntityAnimal animal, IAnimalData data) {
        if (!player.worldObj.isRemote) {
            data.clean(player);
        } else {
            for (int j = 0; j < 30D; j++) {
                double d7 = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                double d8 = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                double d9 = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                animal.worldObj.spawnParticle(EnumParticleTypes.TOWN_AURA, d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
            }
        }

        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living, EnumHand hand) {
        if (living instanceof IAnimalTracked) {
            IAnimalData data = ((IAnimalTracked) living).getData();
            Tool tool = getEnumFromStack(stack);
            if (tool == BRUSH && !(living instanceof EntityChicken)) {
                return clean(player, (EntityAnimal) living, data);
            } else if (tool == MEDICINE) {
                return heal(player, data, stack);
            } else if (tool == MIRACLE_POTION && !(living instanceof EntityChicken)) {
                return impregnate(player, (EntityAnimal) living, data, stack);
            } else if (tool == MILKER && living instanceof IMilkable) {
                return milk(player, hand, ((IMilkable)living));
            }
        }
        return false;
    }



    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return (getEnumFromStack(stack) == MIRACLE_POTION ? AQUA : "") + super.getItemStackDisplayName(stack);
    }

    @Override
    protected String getPrefix(Tool tool) {
        return "animal_tool";
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.GENERAL_ITEM;
    }
}
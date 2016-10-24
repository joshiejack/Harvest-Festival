package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.EntityLivingBase;
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
import java.util.Locale;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.*;
import static net.minecraft.util.text.TextFormatting.AQUA;

public class ItemAnimalTool extends ItemHFEnum<ItemAnimalTool, Tool> implements IShippable {
    private static final double MAX_DAMAGE = 512;
    public enum Tool implements IStringSerializable {
        MILKER(true), BRUSH(true), MEDICINE(false), CHICKEN_FEED(false), MIRACLE_POTION(false);

        private final boolean isDamageable;

        Tool(boolean isDamageable) {
            this.isDamageable = isDamageable;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemAnimalTool() {
        super(Tool.class);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return getEnumFromStack(stack) == CHICKEN_FEED ? 1 : 0;
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

    private final HashMap<EntityPlayer, AnimalStats> milkables = new HashMap<>();

    @Override
    public ItemStack onItemUseFinish(ItemStack held, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && getEnumFromStack(held) == MILKER) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            AnimalStats stats = milkables.get(player);
            if (stats != null) {
                if (stats.performAction(world, player, held, AnimalAction.CLAIM_PRODUCT)) {
                    ItemStack product = stats.getType().getProduct(player, stats);
                    if (!player.inventory.addItemStackToInventory(product)) {
                        player.dropItem(product, false);
                    }

                    int damage = getDamageForDisplay(held) + 1;
                    if (damage >= MAX_DAMAGE) {
                        held.splitStack(1);
                    } else {
                        held.getSubCompound("Data", true).setInteger("Damage", damage);
                    }

                    HFApi.player.getRelationsForPlayer(player).affectRelationship(EntityHelper.getEntityUUID(stats.getAnimal()), stats.getType().getRelationshipBonus(AnimalAction.CLAIM_PRODUCT));
                    ToolHelper.consumeHunger(player, 4F);
                }
            }
        }

        return held;
    }

    private boolean milk(EntityPlayer player, EnumHand hand, ItemStack stack, AnimalStats stats) {
        if (stats.performAction(player.worldObj, player, stack, AnimalAction.TEST_PRODUCT)) {
            milkables.put(player, stats);
            player.setActiveHand(hand);
            return true;
        }

        return false;
    }

    private boolean impregnate(EntityPlayer player, AnimalStats stats, ItemStack stack) {
        if (stats.performAction(player.worldObj, player, stack, AnimalAction.IMPREGNATE)) {
            stack.splitStack(1);
            return true;
        } else return false;
    }

    private boolean heal(EntityPlayer player, AnimalStats stats, ItemStack stack) {
        if (stats.performAction(player.worldObj, player, stack, AnimalAction.HEAL)) {
            stack.splitStack(1);
            ToolHelper.consumeHunger(player, 5F);
            return true;
        } else return false;
    }

    private boolean clean(EntityPlayer player, ItemStack held, EntityLivingBase animal, AnimalStats stats) {
        boolean cleanable = stats.performAction(player.worldObj, player, held, AnimalAction.CLEAN);
        if (cleanable) {
            if (player.worldObj.isRemote) {
                for (int j = 0; j < 30D; j++) {
                    double d7 = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                    double d8 = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                    double d9 = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                    animal.worldObj.spawnParticle(EnumParticleTypes.TOWN_AURA, d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                }
            }

            int damage = getDamageForDisplay(held) + 1;
            if (damage >= MAX_DAMAGE) {
                held.splitStack(1);
            } else {
                held.getSubCompound("Data", true).setInteger("Damage", damage);
            }

            ToolHelper.consumeHunger(player, 4F);
            return true;
        }

        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase living, EnumHand hand) {
        AnimalStats stats = EntityHelper.getStats(living);
        if (stats != null) {
            Tool tool = getEnumFromStack(stack);
            if (tool == BRUSH ) {
                return clean(player, stack, living, stats);
            } else if (tool == MEDICINE) {
                return heal(player, stats, stack);
            } else if (tool == MIRACLE_POTION) {
                return impregnate(player, stats, stack);
            } else if (tool == MILKER) {
                return milk(player, hand, stack, stats);
            }
        }

        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDurabilityForDisplay(stack) > 0D;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)  {
        return canBeDamaged(stack) ? ((double) getDamageForDisplay(stack) / MAX_DAMAGE) : 0;
    }

    protected int getDamageForDisplay(ItemStack stack) {
        return stack.getSubCompound("Data", true).getInteger("Damage");
    }

    public boolean canBeDamaged(ItemStack stack) {
        return getEnumFromStack(stack).isDamageable;
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
package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.data.TraitCleanable;
import uk.joshiejack.husbandry.animals.traits.data.TraitMammal;
import uk.joshiejack.husbandry.HusbandrySounds;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemTool extends ItemMulti<ItemTool.Tool> {
    public ItemTool() {
        super(new ResourceLocation(MODID, "tool"), Tool.class);
        setCreativeTab(Husbandry.TAB);
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return getEnumFromStack(stack) == Tool.MILKER ? 32: 0;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return getEnumFromStack(stack) == Tool.MILKER ? EnumAction.BOW: EnumAction.NONE;
    }

    private final Map<EntityPlayer, Pair<AnimalStats<?>, EnumHand>> milkables = new HashMap<>();

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            Pair<AnimalStats<?>, EnumHand> pair = milkables.get(player);
            if (pair != null && pair.getKey().canProduceProduct()) {
                EnumHand other = pair.getValue() == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                if (player.getHeldItem(other).getItem() == Items.BUCKET) {
                    player.setHeldItem(other, new ItemStack(Items.MILK_BUCKET));
                } else ItemHandlerHelper.giveItemToPlayer(player, pair.getKey().getProduct());

                pair.getKey().setProduced(1);
            }

            milkables.remove(player);
        }

        return stack;
    }

    @Override
    public boolean itemInteractionForEntity(@Nonnull ItemStack stack, EntityPlayer player, @Nonnull EntityLivingBase target, @Nonnull EnumHand hand) {
        AnimalStats<?> stats = AnimalStats.getStats(target);
        World world = player.world;
        if (stats != null) {
            Tool tool = getEnumFromStack(stack);
            if (tool == Tool.BRUSH && stats.hasTrait("cleanable") && ((TraitCleanable)stats.getTrait("cleanable")).clean()) {
                if (world.isRemote) {
                    for (int j = 0; j < 30D; j++) {
                        double d7 = (target.posY - 0.5D) + world.rand.nextFloat();
                        double d8 = (target.posX - 0.5D) + world.rand.nextFloat();
                        double d9 = (target.posZ - 0.5D) + world.rand.nextFloat();
                        world.spawnParticle(EnumParticleTypes.TOWN_AURA, d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                    }
                }

                world.playSound(player, player.posX, player.posY, player.posZ, HusbandrySounds.BRUSH, SoundCategory.PLAYERS, 1.5F, 1F);
            } else if (tool == Tool.MIRACLE_POTION && stats.getType().hasTrait("mammal") && ((TraitMammal)stats.getTrait("mammal")).impregnate()) {
                stack.shrink(1);
                return true;
            } else if (tool == Tool.MILKER && stats.getType().hasTrait("milkable") && stats.canProduceProduct()) {
                milkables.put(player, Pair.of(stats, hand));
                player.setActiveHand(hand);
                return true;
            }
        }

        return false;
    }

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case MIRACLE_POTION:
                return 16;
            case BRUSH:
            case MILKER:
            default:
                return 1;
        }
    }

    public enum Tool {
        BRUSH, MILKER, MIRACLE_POTION
    }
 }

package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemTreat extends ItemMulti<ItemTreat.Treat> {
    public ItemTreat() {
        super(new ResourceLocation(MODID, "treat"), Treat.class);
        setCreativeTab(Husbandry.TAB);
    }

    @Override
    public boolean itemInteractionForEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull EntityLivingBase target, @Nonnull EnumHand hand) {
        AnimalStats<?> stats = AnimalStats.getStats(target);
        boolean generic = getEnumFromStack(stack) == Treat.GENERIC;
        if (stats != null) {
            if (!generic && !stack.isItemEqual(stats.getType().getTreat())) {
                stats.decreaseHappiness(20);
                stack.shrink(1);
                return true;
            }

            boolean treated = stats.treat(generic);
            if (treated) {
                stack.shrink(1);
                return true;
            }
        }

        return false;
    }

    public enum Treat {
        GENERIC,
        CAT,
        CHICKEN,
        COW,
        DOG,
        HORSE,
        PIG,
        RABBIT,
        SHEEP,
        DUCK,
        PARROT
    }
 }

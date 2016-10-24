package joshie.harvest.animals.type;

import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class AnimalCow extends AnimalAbstract {
    public AnimalCow() {
        super("cow", 12, 20, GRASS);
    }

    @Override
    public int getDaysBetweenProduction() {
        return 1;
    }

    @Override
    public int getGenericTreatCount() {
        return 7;
    }

    @Override
    public int getTypeTreatCount() {
        return 24;
    }

    @Override
    public int getRelationshipBonus(AnimalAction action) {
        switch (action) {
            case FEED:          return 10;
            case OUTSIDE:       return 2;
            case CLAIM_PRODUCT: return 10;
        }

        return super.getRelationshipBonus(action);
    }

    @Override
    public ItemStack getProduct(EntityPlayer player, AnimalStats stats) {
        ItemStack product = SizeableHelper.getMilk(player, stats.getAnimal(), stats);
        player.addStat(HFAchievements.milker);
        if (HFCore.SIZEABLE.getSize(product) == Size.LARGE) {
            player.addStat(HFAchievements.milkerLarge);
        }

        return product;
    }
}

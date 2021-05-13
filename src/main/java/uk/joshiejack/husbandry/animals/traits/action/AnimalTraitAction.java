package uk.joshiejack.husbandry.animals.traits.action;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public abstract class AnimalTraitAction extends AnimalTrait {
    public AnimalTraitAction(String name) {
        super(name);
    }

    public abstract boolean onRightClick(AnimalStats<?> stats, EntityAgeable animal, EntityPlayer player, EnumHand hand);
}

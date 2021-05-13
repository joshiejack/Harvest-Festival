package uk.joshiejack.husbandry.animals.traits.action;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

@PenguinLoader("milkable")
public class TraitMilkable extends AnimalTraitAction {
    public TraitMilkable(String name) {
        super(name);
    }

    @Override
    public boolean onRightClick(AnimalStats<?> stats, EntityAgeable animal, EntityPlayer player, EnumHand hand) {
        return player.getHeldItem(hand).getItem() == Items.BUCKET;
    }
}

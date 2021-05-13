package uk.joshiejack.husbandry.animals.traits.action;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@PenguinLoader("pettable")
public class TraitPettable extends AnimalTraitAction {
    public TraitPettable(String name) {
        super(name);
    }

    @Override
    public boolean onRightClick(AnimalStats<?> stats, EntityAgeable animal, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty() && !stats.hasBeenLoved()) {
            return stats.setLoved();
        }

        return false;
    }
}

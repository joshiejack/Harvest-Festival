package uk.joshiejack.husbandry.animals.traits.action;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@PenguinLoader("carriable")
public class TraitCarriable extends AnimalTraitAction {
    public TraitCarriable(String name) {
        super(name);
    }

    @Override
    public boolean onRightClick(AnimalStats<?> stats, EntityAgeable animal, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (hand == EnumHand.MAIN_HAND && !player.isBeingRidden() && held.isEmpty() && !stats.hasBeenLoved()) {
            animal.setEntityInvulnerable(true);
            animal.startRiding(player, true);
            return stats.setLoved();
        }

        return false;
    }
}

package uk.joshiejack.husbandry.animals.traits.action;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;

import java.util.Random;

@PenguinLoader("shearable")
public class TraitShearable extends AnimalTraitAction {
    public TraitShearable(String name) {
        super(name);
    }

    private NonNullList<ItemStack> getDrops(Random rng, ItemStack stack) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < 1 + rng.nextInt(3); i++) {
            list.add(stack);
        }

        return list;
    }

    @Override
    public boolean onRightClick(AnimalStats<?> stats, EntityAgeable animal, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getItem() instanceof ItemShears && stats.canProduceProduct()) {
            if (!player.world.isRemote) {
                Random rand = animal.getRNG();
                for (ItemStack stack : getDrops(animal.getRNG(), stats.getProduct())) {
                    EntityItem ent = animal.entityDropItem(stack, 1.0F);
                    if (ent != null) {
                        ent.motionY += rand.nextFloat() * 0.05F;
                        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    }
                }

                stats.setProduced(5);
            }

            if (animal instanceof EntitySheep) {
                ((EntitySheep) animal).setSheared(true);
            }

            held.damageItem(1, animal);

            return true;
        }

        return false;
    }
}

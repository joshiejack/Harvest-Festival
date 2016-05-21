package joshie.harvest.blocks.tiles;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.helpers.generic.EntityHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileTrough extends TileFillable {
    private static final AnimalFoodType[] GRASS = new AnimalFoodType[] { AnimalFoodType.GRASS };

    @Override
    public boolean onActivated(ItemStack held) {
        if (HFApi.animals.canEat(GRASS, held)) {
            boolean processed = false;
            for (int i = 0; i < 7 && held.stackSize > 0; i++) {
                if (held.stackSize >= 1) {
                    if (hasRoomAndFill()) {
                        held.splitStack(1);
                        processed = true;
                    } else break;
                }
            }

            return processed;
        }

        return false;
    }

    private boolean hasRoomAndFill() {
        if (fillAmount < 7) {
            setFilled(getFillAmount() + 1);
            return true;
        }

        return false;
    }

    private boolean hasFoodAndFeed() {
        if (fillAmount > 0) {
            setFilled(getFillAmount() - 1);
            return true;
        }

        return false;
    }

    @Override
    public void newDay(World world) {
        for (EntityAnimal animal: EntityHelper.getEntities(EntityAnimal.class, world, 32D)) {
            if (animal instanceof IAnimalTracked) { //Feed all the local animals
                if (hasFoodAndFeed()) {
                    ((IAnimalTracked) animal).getData().feed(null);
                } else break;
            }
        }
    }
}
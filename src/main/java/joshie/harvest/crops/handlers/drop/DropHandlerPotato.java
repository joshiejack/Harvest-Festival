package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerPotato extends DropHandler {
    @Override
    public NonNullList<ItemStack> getDrops(Crop crop, int stage, Random rand) {
        NonNullList<ItemStack> list = NonNullList.create();
        if (stage >= crop.getStages()) {
            for (int i = 0; i < (rand.nextInt(20) == 0 ? 2: 1); i++) {
                list.add(rand.nextInt(50) == 0 ? new ItemStack(Items.POISONOUS_POTATO) : new ItemStack(Items.POTATO));
            }
        }

        return list;
    }
}
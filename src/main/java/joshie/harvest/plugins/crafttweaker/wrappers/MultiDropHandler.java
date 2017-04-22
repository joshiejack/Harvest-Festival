package joshie.harvest.plugins.crafttweaker.wrappers;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiDropHandler extends DropHandler<Crop> {
    private final ItemStack[] drops;
    private final int[] amounts;
    private final int[] chances;

    public MultiDropHandler(ItemStack[] drops, int[] amounts, int[] chances) {
        this.drops = drops;
        this.amounts = amounts;
        this.chances = chances;
    }

    @Override
    public List<ItemStack> getDrops(Crop crop, int stage, Random rand) {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < drops.length; i++) {
            if (rand.nextInt(100) < chances[i]) {
                ItemStack stack = drops[i].copy();
                stack.stackSize = 1 + rand.nextInt(amounts[i]);
                list.add(stack);
            }
        }

        return list;
    }
}

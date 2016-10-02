package joshie.harvest.plugins.immersiveengineering;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.DropHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropHandlerHemp extends DropHandler {
    private final Item hemp;

    public DropHandlerHemp(Item hemp) {
        this.hemp = hemp;
    }

    @Override
    public List<ItemStack> getDrops(Crop crop, int stage, Random rand) {
        List<ItemStack> ret = new ArrayList<>();
        if (stage < crop.getStages()) return null;
        for (int i = 0; i < 3; i++) {
            if (rand.nextInt(8) <= StateHandlerHemp.getMetaFromStage(stage)) {
                ret.add(new ItemStack(hemp, 1, 4));
            }
        }

        return ret;
    }
}

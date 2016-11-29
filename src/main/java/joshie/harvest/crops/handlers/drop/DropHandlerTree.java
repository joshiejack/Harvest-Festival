package joshie.harvest.crops.handlers.drop;

import joshie.harvest.api.crops.DropHandler;
import joshie.harvest.api.trees.Tree;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Random;

@SuppressWarnings("unused")
public class DropHandlerTree extends DropHandler<Tree> {
    @Override
    public ItemStack getDrop(Tree tree, int stage, Random rand) {
        return stage >= tree.getStages() ? tree.getWoodStack() : null;
    }
}
package joshie.harvest.api.trees;

import net.minecraft.item.ItemStack;

public interface ITreeRegistry {
    /** Returns the tree as seeds
     * @param tree  tree type
     * @param amount stack size**/
    ItemStack getSeedStack(Tree tree, int amount);

    /** Returns the tree as the fruit item
     * @param tree  tree type
     * @param amount stack size**/
    ItemStack getFruitStack(Tree tree, int amount);

    /** Returns the tree this represents
     * @param stack */
    Tree getTreeFromStack(ItemStack stack);

     enum TreeStage {
        SEED, SAPLING, JUVENILE, MATURE, HARVESTABLE
    }
}

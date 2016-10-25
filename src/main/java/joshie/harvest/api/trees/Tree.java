package joshie.harvest.api.trees;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.GrowthHandler;
import net.minecraft.util.ResourceLocation;

public class Tree extends Crop {
    public static final GrowthHandler TREE_GROWTH = new GrowthHandlerTree();
    private int maturity;

    /** Constructor for tree **/
    public Tree(ResourceLocation key) {
        super(key);
        setGrowthHandler(TREE_GROWTH);
        setNoWaterRequirements();
    }

    /** Returns the stage at which this tree is mature **/
    public int getStagesToMaturity() {
        return maturity;
    }

    /**
     * Creates a state handler based on the passed in values
     */
    public Tree setStageLength(int seeds, int sapling, int juvenile, int maturity) {
        this.maturity = seeds + sapling + juvenile + maturity;
        setStages(this.maturity - maturity);
        setRegrow(this.maturity - 3);
        setStateHandler(new StateHandlerTree(seeds, sapling, juvenile));
        return this;
    }

    /**
     * Set how many days it takes fruit to regrow
     * **/
    public Tree setFruitRegrow(int regrow) {
        setRegrow(getStagesToMaturity() - regrow);
        return this;
    }
}
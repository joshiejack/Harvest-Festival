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

    /** Set the stage at which this tree becomes mature
     *  @param maturity     the stage of maturity */
    public Tree setMaturity(int maturity) {
        this.maturity = maturity;
        return this;
    }

    /** Returns the stage at which this tree is mature **/
    public int getStagesToMaturity() {
        return maturity;
    }
}
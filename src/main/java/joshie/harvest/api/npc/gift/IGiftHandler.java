package joshie.harvest.api.npc.gift;

import net.minecraft.item.ItemStack;

/** This is attached to npcs to test for their favourite gifts **/
public interface IGiftHandler {
    /** Return the quality of this gift based on the passed in stack
     *  @param stack    the item
     * @return  the quality of the item in the npcs opinion */
    default Quality getQuality(ItemStack stack) {
        return Quality.DECENT;
    }

    /** Quality of the gifts **/
    enum Quality {
        AWESOME(1000), GOOD(500), DECENT(200), DISLIKE(-200), BAD(-400), TERRIBLE(-800);

        private final int points;

        Quality(int points) {
            this.points = points;
        }

        public int getRelationPoints() {
            return points;
        }
    }
}

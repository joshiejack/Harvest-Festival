package uk.joshiejack.settlements.tile;

import uk.joshiejack.settlements.block.AdventureBlocks;
import uk.joshiejack.penguinlib.tile.TilePenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.EnumFacing;

@PenguinLoader("quest_board")
public class TileQuestBoard extends TilePenguin {
    public EnumFacing getFacing() {
        return AdventureBlocks.QUEST_BOARD.getFacing(getState());
    }
}

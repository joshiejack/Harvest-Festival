package joshie.harvest.blocks.tiles;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileNest extends TileFillable implements IDailyTickable {
    private static final int MAX_FILL = 7;

    @Override
    public boolean onActivated(ItemStack held) {
        if (ToolHelper.isEgg(held)) {
            if (fillAmount == 0) {
                setFilled(MAX_FILL);
                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    @Override
    public void newDay(World world) {

    }


}
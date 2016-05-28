package joshie.harvest.animals.blocks;

import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.blocks.tiles.TileFillable;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;

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
        if (fillAmount > 0) {
            fillAmount--;

            if (fillAmount == 0) {
                EntityChicken chicken = new EntityChicken(worldObj);
                chicken.setPositionAndUpdate(getPos().getX() + 3 * world.rand.nextDouble(), getPos().getY() + world.rand.nextDouble(), getPos().getZ() + 3 * world.rand.nextDouble());
                worldObj.spawnEntityInWorld(chicken);
            }

            sendRefreshPacket(this);
            markDirty();
        }
    }
}
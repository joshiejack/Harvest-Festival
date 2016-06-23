package joshie.harvest.animals.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.tiles.TileFillableSizedFaceable;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;

public class TileIncubator extends TileFillableSizedFaceable {
    private static final int MAX_FILL = 7;

    @Override
    public boolean onActivated(ItemStack held) {
        if (ToolHelper.isEgg(held)) {
            if (fillAmount == 0) {
                setFilled(HFApi.sizeable.getSizeableFromStack(held).getRight(), MAX_FILL);
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
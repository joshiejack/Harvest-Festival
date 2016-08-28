package joshie.harvest.animals.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.base.tile.TileFillableSizedFaceable;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;

import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;

public class TileIncubator extends TileFillableSizedFaceable {
    private static final int MAX_FILL = 7;

    @Override
    public boolean onActivated(ItemStack held) {
        if (ToolHelper.isEgg(held)) {
            if (fillAmount == 0) {
                setFilled(HFApi.sizeable.getSize(held), MAX_FILL);
                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    @Override
    public void newDay() {
        if (fillAmount > 0) {
            fillAmount--;

            if (fillAmount == 0) {
                EntityChicken chicken = new EntityChicken(worldObj);
                chicken.setPositionAndUpdate(getPos().getX() + 3 * getWorld().rand.nextDouble(), getPos().getY() + getWorld().rand.nextDouble(), getPos().getZ() + 3 * getWorld().rand.nextDouble());
                worldObj.spawnEntityInWorld(chicken);
            }

            sendRefreshPacket(this);
            markDirty();
        }
    }
}
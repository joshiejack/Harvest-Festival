package uk.joshiejack.economy.inventory;

import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerShop extends ContainerPenguinInventory {
      public ContainerShop() {
        super(0);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {}
}

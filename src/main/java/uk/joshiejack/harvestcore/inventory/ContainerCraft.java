package uk.joshiejack.harvestcore.inventory;

import uk.joshiejack.penguinlib.inventory.ContainerBook;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;

@PenguinLoader(value = "0")
public class ContainerCraft extends ContainerBook {
    public ContainerCraft(EntityPlayer player) {
        //Get all the blueprints
        super(1);

        int start = 100;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(createSlot(player.inventory, j + i * 9 + 9, start + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(createSlot(player.inventory, i, start + i * 18, 142));
        }
    }
}

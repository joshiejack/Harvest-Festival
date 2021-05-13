package uk.joshiejack.penguinlib.data.custom.item;

import uk.joshiejack.penguinlib.data.custom.ICustom;
import net.minecraft.item.ItemStack;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;

public interface ICustomItem extends ICustom {
    void init();
    AbstractCustomData.ItemOrBlock getDataFromStack(ItemStack stack);
}

package uk.joshiejack.settlements.entity.ai.action.item;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("take_item")
public class ActionTake extends ActionMental {
    private String holder;
    private int amount;

    @Override
    public ActionTake withData(Object... params) {
        this.holder = (String) params[0];
        this.amount = (int) params[1];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) InventoryHelper.takeItemsInInventory(player, Holder.getFromString(holder), amount);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Holder", holder);
        tag.setShort("Amount", (short) amount);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        holder = nbt.getString("Holder");
        amount = nbt.getShort("Amount");
    }
}

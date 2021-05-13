package uk.joshiejack.settlements.entity.ai.action.item;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

@PenguinLoader("take_held")
public class ActionTakeHeldItem extends ActionMental {
    private EnumHand hand;
    private int amount;

    @Override
    public ActionTakeHeldItem withData(Object... params) {
        this.hand = (EnumHand) params[0];
        this.amount = (int) params[1];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) player.getHeldItem(hand).shrink(amount);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("MainHand", hand == EnumHand.MAIN_HAND);
        tag.setByte("Amount", (byte) amount);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        hand = nbt.getBoolean("MainHand") ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        amount = nbt.getByte("Amount");
    }
}

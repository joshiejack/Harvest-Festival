package joshie.harvest.cooking.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.base.TileFaceable;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/** Just a way to interact with the fridge inventory, the fridge inventory is global though, not stored in this block **/
public class TileFridge extends TileFaceable {
    protected FridgeData data = new FridgeData(this);

    public static boolean isValid(ItemStack stack) {
        return stack.getItem() == HFCooking.MEAL || HFApi.cooking.getCookingComponents(stack).size() > 0 || stack.getItem() instanceof ItemFood;
    }

    public FridgeData getContents() {
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        data.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        return data.writeToNBT(nbt);
    }
}
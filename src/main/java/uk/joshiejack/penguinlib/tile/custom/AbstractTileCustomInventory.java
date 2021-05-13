package uk.joshiejack.penguinlib.tile.custom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.block.custom.BlockMultiCustomInventory;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("custom_inventory")
public class AbstractTileCustomInventory<T extends AbstractTileCustomInventory<?>> extends TileInventory {
    protected Interpreter script = null;

    public AbstractTileCustomInventory() {
        super(1);
    }

    @SuppressWarnings("unchecked")
    public T withData(IBlockState state, BlockMultiCustomInventory data) {
        this.script = Scripting.getScript(data.getScript(state));
        this.handler = createHandler(Scripting.getResult(script, "getInventorySize", 1));
        return (T) this;
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return Scripting.getResult(script, "isStackValidForInsertion", false, slot, stack);
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return Scripting.getResult(script, "isSlotValidForExtraction", false, slot, amount);
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        return Scripting.getResult(script, "onRightClicked", false, player, hand);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        //Recreate the handler
        if (nbt.hasKey("Script")) script = Scripting.getScript(new ResourceLocation(nbt.getString("Script")));
        this.handler = createHandler(Scripting.getResult(script, "getInventorySize", 1));
        handler.deserializeNBT(nbt.getCompoundTag("Inventory"));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Inventory", handler.serializeNBT());
        if (script != null) nbt.setString("Script", script.this_id.toString());
        return super.writeToNBT(nbt);
    }
}

package uk.joshiejack.settlements.entity.ai.action.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.logging.log4j.Level;

@PenguinLoader("give_item")
public class ActionGiftItem extends ActionMental {
    private ItemStack stack;

    @Override
    public ActionGiftItem withData(Object... params) {
        if (params[0] instanceof String) {
            Settlements.logger.log(Level.WARN, "Tried to use a string for a gift item action instead of creating an item!");
        }
        
        if (params[0] instanceof ItemStack) this.stack = (ItemStack) params[0];
        else {
            assert params[0] instanceof ItemStackJS;
            this.stack = ((ItemStackJS) params[0]).penguinScriptingObject;
        }
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        StackHelper.writeToNBT(stack, tag);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        stack = StackHelper.readFromNBT(tag);
    }
}

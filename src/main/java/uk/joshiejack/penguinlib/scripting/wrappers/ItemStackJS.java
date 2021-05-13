package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.Matcher;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class ItemStackJS extends AbstractJS<ItemStack> {
    public static final ItemStackJS EMPTY = new ItemStackJS(ItemStack.EMPTY);

    public ItemStackJS(ItemStack stack) {
        super(stack);
    }

    public boolean isEmpty() {
        return penguinScriptingObject.isEmpty();
    }

    @SuppressWarnings("ConstantConditions")
    public boolean is(String item) {
        ItemStack object = penguinScriptingObject;
        if (Matcher.ORE.matches(object, item)) return true;
        if (ItemStack.areItemsEqual(StackHelper.getStackFromString(item), object)) return true;
        String name = item.contains(":") ? item: "minecraft:" + item;
        return ForgeRegistries.ITEMS.getKey(object.getItem()).toString().equalsIgnoreCase(name);
    }

    public DataJS data() {
        ItemStack object = penguinScriptingObject;
        NBTTagCompound tag = object.hasTagCompound() ? object.getTagCompound(): new NBTTagCompound();
        object.setTagCompound(tag); //Force it on
        return WrapperRegistry.wrap(Objects.requireNonNull(tag));
    }

    public ItemStackJS setCount(int count) {
        penguinScriptingObject.setCount(count);
        return this;
    }

    public String name() {
        return penguinScriptingObject.getDisplayName();
    }

    public int count() {
        return penguinScriptingObject.getCount();
    }

    public void shrink(int amount) {
        penguinScriptingObject.shrink(amount);
    }

    public void grow(int amount) { penguinScriptingObject.grow(amount); }

    public ItemStackJS setStack(String item) {
        NBTTagCompound tag = NBTHelper.getItemNBT(penguinScriptingObject);
        ItemStack internal = StackHelper.getStackFromString(item);
        internal.writeToNBT(tag);
        return this;
    }
}

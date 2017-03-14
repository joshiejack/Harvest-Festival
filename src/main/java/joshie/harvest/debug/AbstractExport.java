package joshie.harvest.debug;

import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("all")
public abstract class AbstractExport<E extends Enum> {
    public final Map<E, String> LOCATIONS;
    public final Map<E, String> DESCRIPTIONS;
    public final Map<E, String> IS;
    private final BlockHFEnum block;
    private final ItemHFEnum item;
    private final ItemHFFoodEnum food;

    public AbstractExport(Class<E> e, ItemHFEnum item) {
        this.item = item;
        this.block = null;
        this.food = null;
        LOCATIONS = new EnumMap<>(e);
        DESCRIPTIONS = new EnumMap<>(e);
        IS = new EnumMap<>(e);
    }

    public AbstractExport(Class<E> e, BlockHFEnum block) {
        this.item = null;
        this.block = block;
        this.food = null;
        LOCATIONS = new EnumMap<>(e);
        DESCRIPTIONS = new EnumMap<>(e);
        IS = new EnumMap<>(e);
    }

    public AbstractExport(Class<E> e, ItemHFFoodEnum food) {
        this.item = null;
        this.block = null;
        this.food = food;
        LOCATIONS = new EnumMap<>(e);
        DESCRIPTIONS = new EnumMap<>(e);
        IS = new EnumMap<>(e);
    }

    protected void register(E e, String location, String desc)  {
        LOCATIONS.put(e, location);
        DESCRIPTIONS.put(e, desc);
    }

    protected void register(E e, String location, String is, String desc)  {
        IS.put(e, is);
        LOCATIONS.put(e, location);
        DESCRIPTIONS.put(e, desc);
    }

    protected E getEnumFromStack(ItemStack stack) {
        return (E) (item != null ? item.getEnumFromStack(stack) : food != null? food.getEnumFromStack(stack): block.getEnumFromStack(stack));
    }

    protected boolean isExportable(ItemStack stack) {
        if (item != null) return stack.getItem() == item;
        if (food != null) return stack.getItem() == food;
        return stack.getItem() == Item.getItemFromBlock(block);
    }

    protected void export(ItemStack stack, String... parameters) {
        E e = getEnumFromStack(stack);
        export(stack, getEnumFromStack(stack));
    }

    protected abstract void export(ItemStack stack, E eNum);
}

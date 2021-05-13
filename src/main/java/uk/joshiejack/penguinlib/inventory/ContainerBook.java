package uk.joshiejack.penguinlib.inventory;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.lang.reflect.InvocationTargetException;

public class ContainerBook extends ContainerPenguinInventory {
    public static final Byte2ObjectMap<Class<? extends ContainerBook>> REGISTRY = new Byte2ObjectOpenHashMap<>();

    public ContainerBook(int inventorySize) {
        super(inventorySize);
    }

    public static Container getContainerFromID(EntityPlayer player, int id) {
        if (id == -1) return new ContainerBook(0);
        try {
            return ContainerBook.REGISTRY.get((byte) id).getConstructor(EntityPlayer.class).newInstance(player);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return new ContainerBook(0);
        }
    }
}

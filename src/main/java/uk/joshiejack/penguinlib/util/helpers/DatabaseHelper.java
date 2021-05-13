package uk.joshiejack.penguinlib.util.helpers;

import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderItem;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;

public class DatabaseHelper {
    public static void registerSimpleMachine(DatabaseLoadedEvent.LoadComplete event, String name, HolderRegistry<ItemStack> registry) {
        event.table(name).rows().forEach(row -> {
            Holder holder = Holder.getFromString(row.get("input"));
            ItemStack output = StackHelper.getStackFromString(row.get("output"));
            if (holder != HolderItem.EMPTY && !output.isEmpty()) {
                registry.register(holder, output);
            }
        });
    }
}

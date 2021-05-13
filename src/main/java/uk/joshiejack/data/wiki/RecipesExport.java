package uk.joshiejack.data.wiki;

import uk.joshiejack.data.Data;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.penguinlib.data.holder.Holder;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class RecipesExport implements Runnable {
    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name,Utensil,Ingredients,From\n");
        for (Map.Entry<Holder, Long> e: ShippingRegistry.INSTANCE.getEntries()) {
            for (ItemStack stack: e.getKey().getStacks()) {
                builder.append(stack.getDisplayName());
                builder.append(",");
                builder.append(e.getValue());
                builder.append("\n");
            }
        }

        Data.output("shipping.csv", builder);
    }
}

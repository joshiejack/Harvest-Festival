package uk.joshiejack.economy.shop.handler;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@PenguinLoader("bundle")
public class BundleListingHandler extends ListingHandler<List<Pair<ListingHandler, Object>>> {
    @Override
    public String getType() {
        return "bundle";
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemStack[] createIcon(List<Pair<ListingHandler, Object>> pairs) {
        ItemStack[] stacks = new ItemStack[pairs.size()];
        for (int i = 0; i < pairs.size(); i++) {
            stacks[i] = pairs.get(i).getKey().createIcon(pairs.get(i).getValue())[0];
        }

        return stacks;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void purchase(EntityPlayer player, List<Pair<ListingHandler, Object>> list) {
        list.forEach(pair -> pair.getKey().purchase(player, pair.getValue()));
    }

    @Override
    public List<Pair<ListingHandler, Object>> getObjectFromDatabase(Database database, String data) {
        List<Pair<ListingHandler, Object>> list = Lists.newArrayList();
        database.table("bundles").where("id="+data).forEach(row -> {
            ListingHandler handler = Listing.HANDLERS.get(row.get("handler").toString());
            Object entry = handler.getObjectFromDatabase(database, row.get("data"));
            list.add(Pair.of(handler, entry));
        });

        return list;
    }

    @Override
    public boolean isValid(List<Pair<ListingHandler, Object>> pairs) {
        return !pairs.isEmpty();
    }

    @Override
    public String getValidityError() {
        return "No objects were valid";
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public void addTooltip(List<String> list, List<Pair<ListingHandler, Object>> pairs) {
        pairs.forEach(p -> p.getKey().addTooltip(list, p.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDisplayName(List<Pair<ListingHandler, Object>> pairs) {
        ListingHandler handle = pairs.get(0).getKey();
        return handle.getDisplayName(pairs.get(0).getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getStringFromObject(List<Pair<ListingHandler, Object>> pairs) {
        StringBuilder builder = new StringBuilder();
        for (Pair<ListingHandler, Object> pair: pairs) {
            builder.append(pair.getKey().getType());
            builder.append("#");
            builder.append(pair.getKey().getStringFromObject(pair.getValue()));
            builder.append(",");
        }

        return builder.toString().substring(0 , builder.toString().length() - 1);
    }
}

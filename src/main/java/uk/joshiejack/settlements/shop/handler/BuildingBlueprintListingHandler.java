package uk.joshiejack.settlements.shop.handler;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.economy.shop.handler.ItemListingHandler;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@PenguinLoader("building_blueprint")
public class BuildingBlueprintListingHandler extends ItemListingHandler {
    protected final List<Pair<ItemStack, Long>> items = Lists.newArrayList();

    @Override
    public String getType() {
        return "building_blueprint";
    }

    @Override
    public String getValidityError() {
        return "Building does not exist";
    }

    @Override
    public ItemStack getObjectFromDatabase(Database database, String data) {
        return AdventureItems.BLUEPRINT.getStackFromResource(new ResourceLocation(data));
    }
}

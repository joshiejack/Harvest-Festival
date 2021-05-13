package uk.joshiejack.harvestcore.shop.purchasable.handler;

import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@PenguinLoader("blueprint")
public class BlueprintListingHandler extends ListingHandler<Blueprint> {
    @Override
    public String getType() {
        return "blueprint";
    }

    @Override
    public String getDisplayName(Blueprint blueprint) {
        return blueprint.getLocalizedName();
    }

    @Override
    public ItemStack[] createIcon(Blueprint blueprint) {
        return new ItemStack[] { blueprint.getResult() };
    }

    @Override
    public void purchase(EntityPlayer player, Blueprint blueprint) {
        if (!player.world.isRemote) {
            blueprint.unlock(player);
        }
    }

    @Override
    public Blueprint getObjectFromDatabase(Database database, String data) {
        return Blueprint.REGISTRY.get(new ResourceLocation(data));
    }

    @Override
    public String getStringFromObject(Blueprint blueprint) {
        return blueprint.getRegistryName().toString();
    }

    @Override
    public boolean isValid(Blueprint blueprint) {
        return Blueprint.REGISTRY.containsKey(blueprint.getRegistryName());
    }

    @Override
    public String getValidityError() {
        return "Recipe does not exist";
    }


}

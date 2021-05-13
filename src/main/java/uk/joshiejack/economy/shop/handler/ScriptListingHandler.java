package uk.joshiejack.economy.shop.handler;

import joptsimple.internal.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

@PenguinLoader("script")
public class ScriptListingHandler extends ListingHandler<ResourceLocation> {
    @Override
    public String getType() {
        return "script";
    }

    @Override
    public ResourceLocation getObjectFromDatabase(Database database, String data) {
        return new ResourceLocation(data.replace("/", "_"));
    }

    @Override
    public String getStringFromObject(ResourceLocation resourceLocation) {
        return resourceLocation.toString();
    }

    @Override
    public boolean isValid(ResourceLocation created) {
        return Scripting.scriptExists(created);
    }

    @Override
    public String getValidityError() {
        return "Script does not exist";
    }

    @Override
    public String getDisplayName(ResourceLocation resource) {
        return StringHelper.localize(Scripting.getResult(resource, "getDisplayName", Strings.EMPTY));
    }

    @Override
    public ItemStack[] createIcon(ResourceLocation resource) {
        return new ItemStack[] { Scripting.getResult(resource, "getDisplayStack", ItemStackJS.EMPTY).penguinScriptingObject };
    }

    @Override
    public void purchase(EntityPlayer player, ResourceLocation resource) {
        Scripting.get(resource).callFunction("purchase", player);
    }
}

package uk.joshiejack.economy.shop.handler;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@PenguinLoader("item")
public class ItemListingHandler extends ListingHandler<ItemStack> {
    protected final List<Pair<ItemStack, Long>> items = Lists.newArrayList();

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public ItemStack getObjectFromDatabase(Database database, String data) {
        return StackHelper.getStackFromString(data);
    }

    @Override
    public String getStringFromObject(ItemStack stack) {
        return StackHelper.getStringFromStack(stack);
    }

    @Override
    public String getValidityError() {
        return "Item does not exist";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list, ItemStack stack) {
        list.addAll(stack.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL));
    }

    @Override
    public boolean isValid(ItemStack stack) {
        return !stack.isEmpty();
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        return stack.getDisplayName();
    }

    @Override
    public ItemStack[] createIcon(ItemStack stack) {
        return new ItemStack[] { stack };
    }

    @Override
    public void purchase(EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
        }
    }
}

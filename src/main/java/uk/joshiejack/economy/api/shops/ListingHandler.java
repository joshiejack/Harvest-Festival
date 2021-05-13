package uk.joshiejack.economy.api.shops;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.data.database.Database;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public abstract class ListingHandler<T> {
    protected final List<Pair<T, Long>> items = Lists.newArrayList();
    public abstract String getType();
    public abstract ItemStack[] createIcon(T t);
    public abstract void purchase(EntityPlayer player, T t);
    public abstract boolean isValid(T t);
    public abstract String getDisplayName(T t);
    public abstract String getStringFromObject(T t);
    public abstract String getValidityError();

    @SideOnly(Side.CLIENT)
    public void addTooltip(List<String> list, T t) {}
    public abstract T getObjectFromDatabase(Database database, String data);
}

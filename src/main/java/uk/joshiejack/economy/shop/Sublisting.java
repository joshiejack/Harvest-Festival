package uk.joshiejack.economy.shop;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

public class Sublisting<P> {
    private final String sub_id;
    private final ListingHandler<P> handler;
    private final P object;
    private Listing listing = null;
    private int int_id;
    private List<MaterialCost> materials = Lists.newArrayList();
    private List<String> tooltip = Lists.newArrayList();
    private CyclingStack icon;
    private String displayName = "";
    private long gold;
    private double weight = 1;

    public Sublisting(String sub_id, ListingHandler<P> handler, P object) {
        this.sub_id = sub_id;
        this.handler = handler;
        this.object = object;
    }

    public Sublisting setIntID(int id) {
        this.int_id = id;
        return this;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public int int_id() {
        return int_id;
    }

    public void addMaterial(MaterialCost cost) {
        this.materials.add(cost);
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String id() {
        return sub_id;
    }

    public double getWeight() {
        return weight;
    }

    public P getObject() {
        return object;
    }

    public long getGold() {
        return gold;
    }

    public List<MaterialCost> getMaterials() {
        return materials;
    }

    public void purchase(EntityPlayer player) {
        handler.purchase(player, object);
        materials.forEach(m -> m.fulfill(player));
    }

    public ListingHandler<P> getHandler() {
        return handler;
    }

    public String getDisplayName() {
        return !displayName.isEmpty() ? displayName : icon != null ? icon.getStack(0).getDisplayName() : handler.getDisplayName(object);
    }

    public boolean isGoldOnly() {
        return materials.isEmpty();
    }

    public ItemStack getDisplayStack() {
        if (icon == null) {
            icon = new CyclingStack(NonNullList.from(ItemStack.EMPTY, handler.createIcon(object)));
        }

        return icon.getStack(0);
    }

    @SideOnly(Side.CLIENT)
    public void addTooltip(List<String> list) {
        if (tooltip.size() == 0) handler.addTooltip(list, object);
        else list.addAll(tooltip);
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = Lists.newArrayList(tooltip);
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public void setDisplayIcon(NonNullList<ItemStack> icon) {
        this.icon = new CyclingStack(icon);
    }

    public boolean hasMaterialRequirement(EntityPlayer player, int amount) {
        return materials.stream().allMatch(m -> m.isMet(player, amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sublisting<?> that = (Sublisting<?>) o;
        return Objects.equals(sub_id, that.sub_id) && Objects.equals(listing, that.listing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sub_id, listing);
    }
}

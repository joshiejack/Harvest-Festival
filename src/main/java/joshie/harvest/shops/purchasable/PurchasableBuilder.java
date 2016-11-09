package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IPurchaseableMaterials;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.buildings.BuildingImpl;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.shops.requirement.Logs;
import joshie.harvest.shops.requirement.Stone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchasableBuilder extends PurchasableFML<BuildingImpl> implements IPurchaseableMaterials {
    private final String resource;
    private ItemStack stack;
    private final IRequirement[] requirements;

    public PurchasableBuilder(long cost, int logs, int stone, ResourceLocation name) {
        super(cost, name);
        if (logs != 0 && stone == 0) requirements = new IRequirement[] { Logs.of(logs) };
        else if (logs == 0 && stone != 0) requirements = new IRequirement[] { Stone.of(stone) };
        else requirements = new IRequirement[] { Logs.of(logs), Stone.of(stone) };
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + name.toString().replace(":", "_");
    }

    public PurchasableBuilder(long cost, ResourceLocation name, IRequirement... requirements) {
        super(cost, name);
        this.requirements = requirements;
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + name.toString().replace(":", "_");
    }

    public PurchasableBuilder(long cost, int logs, int stone, ItemStack stack) {
        super(cost, null);
        this.stack = stack;
        if (logs != 0 && stone == 0) requirements = new IRequirement[] { Logs.of(logs) };
        else if (logs == 0 && stone != 0) requirements = new IRequirement[] { Stone.of(stone) };
        else requirements = new IRequirement[] { Logs.of(logs), Stone.of(stone) };
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + Purchasable.stackToString(stack);
    }

    public PurchasableBuilder(long cost, ItemStack stack, IRequirement... requirements) {
        super(cost, null);
        this.stack = stack;
        this.requirements = requirements;
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + Purchasable.stackToString(stack);
    }

    private boolean isWoodOnly(long cost, int logs, int stone) {
        return logs != 0 && stone == 0 && cost == 0;
    }

    private boolean isStoneOnly(long cost, int logs, int stone) {
        return stone != 0 && logs == 0 && cost == 0;
    }

    @Override
    public IRequirement[] getRequirements() {
        return requirements;
    }

    @Override
    public IForgeRegistry<BuildingImpl> getRegistry() {
        return BuildingRegistry.REGISTRY;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        for (IRequirement requirement: requirements) {
            if (!requirement.isFulfilled(world, player, amount)) return false;
        }

        return isPurchaseable(world, player);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack != null ? stack : HFBuildings.BLUEPRINTS.getStackFromObject(item);
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        for (IRequirement requirement: requirements) {
            requirement.onPurchased(player);
        }

        super.onPurchased(player);
    }

    public boolean isPurchaseable(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return stack != null ? stack.getDisplayName() : getDisplayStack().getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + getDisplayName());
        if (this.tooltip != null) {
            list.add("---------------------------");
            String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
            list.addAll(Arrays.asList(tooltip.split("\r\n")));
        }
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}

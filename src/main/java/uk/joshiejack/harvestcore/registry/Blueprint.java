package uk.joshiejack.harvestcore.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.harvestcore.data.custom.blueprint.CustomBlueprintData;
import uk.joshiejack.harvestcore.network.blueprint.PacketUnlockBlueprint;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import java.util.Map;

public class Blueprint extends PenguinRegistry.Item<Blueprint> {
    public static final Map<ResourceLocation, Blueprint> REGISTRY = Maps.newHashMap();
    public static final Multimap<String, Blueprint> CATEGORIES = HashMultimap.create();
    public static final Map<String, ItemStack> ICONS = Maps.newHashMap();
    private final String toInitResult;
    private final CustomBlueprintData.Requirement[] toInitRequirements;
    private final String category;
    private ItemStack result;
    private Object2IntMap<Holder> requirements;

    public Blueprint(ResourceLocation resource, String category, String result, CustomBlueprintData.Requirement[] requirements) {
        super("blueprint", REGISTRY, resource);
        this.category = category;
        this.toInitResult = result;
        this.toInitRequirements = requirements;
        CATEGORIES.get(category).add(this);
    }

    @Override
    public String getLocalizedName() {
        return result.getDisplayName();
    }

    public String category() {
        return category;
    }

    public boolean init() {
        this.result = StackHelper.getStackFromString(toInitResult);
        if (this.result.isEmpty()) return false;
        else {
            this.requirements = new Object2IntOpenHashMap<>();
            for (CustomBlueprintData.Requirement rq: toInitRequirements) {
                Holder h = Holder.getFromString(rq.item);
                if (h.isEmpty()) return false;
                else requirements.put(h, rq.count);
            }
        }

        return true;
    }

    public ItemStack getResult() {
        if (result == null || result.isEmpty()) {
            PenguinLib.logger.error("The blueprint: " + getRegistryName() + " is corrupted and has no result item!");
            return ItemStack.EMPTY;
        }
        return result;
    }

    public boolean isUnlocked(EntityPlayer player) {
        return player.getEntityData().getCompoundTag("Blueprints").hasKey(getRegistryName().toString());
    }

    public boolean hasAllItems(EntityPlayer player, int multiplier) {
        return requirements.keySet().stream()
                .allMatch(h -> InventoryHelper.hasInInventory(player, h, multiplier * requirements.get(h)));
    }

    public boolean craft(EntityPlayer player) {
        requirements.keySet().forEach(h -> InventoryHelper.takeItemsInInventory(player, h, requirements.get(h)));
        //ItemHandlerHelper.giveItemToPlayer(player, result);
        return true;
    }

    public void unlock(EntityPlayer player) {
        PlayerHelper.setTag(player, "Blueprints", getRegistryName().toString());
        if (!player.world.isRemote) {
            PenguinNetwork.sendToClient(new PacketUnlockBlueprint(this), player);
        }
    }

    public Object2IntMap<Holder> getRequirements() {
        return requirements;
    }
}

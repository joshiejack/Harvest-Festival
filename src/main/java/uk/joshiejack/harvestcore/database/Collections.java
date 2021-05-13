package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class Collections {
    public static final Map<String, Collection> ALL = Maps.newHashMap();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("collections").rows().forEach(row ->
                ALL.put(row.get("id"), new Collection(row.get("id"), row.get("name"), row.get("texture"), row.get("x"), row.get("y"), row.getScript())));
    }

    public static class Collection {
        private final List<NonNullList<ItemStack>> list = Lists.newArrayList();
        private final Page.Icon icon;
        private final ResourceLocation script;
        private final String name;
        private final String id;

        public Collection(String id, String name, String texture, int x, int z, ResourceLocation script) {
            this.id = id;
            this.name = name;
            this.icon = texture.contains(".png") ? new Page.Icon(new ResourceLocation(texture), x, z) : new Page.Icon(StackHelper.getStackFromString(texture), x, z);
            this.script = script;
        }

        public String getID() {
            return id;
        }

        public List<NonNullList<ItemStack>> getList() {
            //Fill the list
            if (list.size() == 0) {
                if (isShippingCollection()) {
                    ShippingRegistry.INSTANCE.getEntries().stream()
                            .filter(h -> !h.getKey().getStacks().isEmpty() &&
                                    isInCollection(h.getKey().getStacks().get(0)))
                            .map(entry -> entry.getKey().getStacks()).forEach(list::add);
                } else StackHelper.getAllItems().stream().filter(this::isInCollection).forEach(stack -> list.add(NonNullList.from(ItemStack.EMPTY, stack)));
            }

            return list;
        }

        @Nullable
        public String formatValue(EntityPlayer player, ItemStack stack, long value) {
            if (Scripting.functionExists(script, "formatValue")) {
                return Scripting.getResult(script, "formatValue", Strings.EMPTY + value, player, stack, value);
            } else return value > 0 ? value + "G" : null;
        }

        public String getName() {
            return name;
        }

        public boolean isInCollection(ItemStack stack) {
            return Scripting.get(script).isTrue("isInCollection", stack);
        }

        public boolean isObtained(EntityPlayer player, ItemStack stack) {
            return Scripting.get(script).isTrue("isObtained", player, stack);
        }

        public Page.Icon getIcon() {
            return icon;
        }


        @Nullable
        public Quality getQuality(EntityPlayer player, ItemStack stack) {
            if (Scripting.functionExists(script, "formatValue")) {
                String result = Scripting.getResult(script, "getQuality", Strings.EMPTY, player, stack);
                return result.isEmpty() ? null : Quality.REGISTRY.get(new ResourceLocation(result));
            } else return null;
        }

        @Deprecated
        public int get(String function, EntityPlayer player, ItemStack stack) {
            Integer result = (Integer) Scripting.get(script).getResultOfFunction(function, player, stack);
            return result == null ? 0 : result;
        }

        public boolean isShippingCollection() {
            return !Scripting.functionExists(script, "getValue");
        }
    }
}

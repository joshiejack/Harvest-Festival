package uk.joshiejack.harvestcore.database;

import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import uk.joshiejack.economy.event.ItemGetValueEvent;
import uk.joshiejack.energy.events.AddFoodStatsEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.harvestcore.ticker.HasQuality;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class QualityEvents {
    private static final Set<Item> HAS_QUALITY = Sets.newHashSet();
    public static boolean hasQuality(Item item) {
        return HAS_QUALITY.contains(item);
    }
    private static ResourceLocation script;

    @SubscribeEvent
    public static void onDatabaseInit(DatabaseLoadedEvent.DataLoaded event) {
        event.table("quality").rows().forEach(row -> {
            if (row.get("name").toString().equalsIgnoreCase("script")) {
                QualityEvents.script = new ResourceLocation(row.get("modifier").toString());
            } else {
                Quality quality = new Quality(new ResourceLocation(row.get("name")), row.getAsDouble("modifier"));
                if (!row.isEmpty("model")) quality.setModel(row.get("model"));
            }
        });
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("quality_replacements").rows().stream()
                .map(row -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(row.get("item"))))
                .filter(Objects::nonNull)
                .forEach(HAS_QUALITY::add);
    }

    @Nullable
    public static Quality getQualityFromString(String string) {
        try {
            return Quality.REGISTRY.get(new ResourceLocation(string));
        } catch (Exception ex) { return null;}
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onDrop(HarvestDropsEvent event) {
        DailyTicker entry = TickerHelper.getTicker(event.getWorld(), event.getPos());
        if (entry instanceof HasQuality) {
            Quality quality = ((HasQuality)entry).getQuality();
            if (quality != null) {
                event.getDrops().stream().filter(stack -> stack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) || hasQuality(stack.getItem())).forEach(stack -> {
                    if (quality.modifier() != 1D) {
                        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
                        stack.getTagCompound().setString("Quality", quality.getRegistryName().toString());
                        stack.getItem().setHasSubtypes(true);
                    }
                });
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    @Optional.Method(modid = "economy")
    public static void modifySellValue(ItemGetValueEvent event) {
        long value = event.getValue();
        if (value > 0) {
            ItemStack stack = event.getStack();
            double modifier = 1D;
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Quality")) {
                Quality quality = QualityEvents.getQualityFromString(stack.getTagCompound().getString("Quality"));
                event.setNewValue((long)(value * quality.modifier()));
            }
        }
    }

    @Nullable
    public static Quality getQualityFromScript(String function, Object... objects) {
        if (script == null || Scripting.get(script) == null) return null;
        else {
            Interpreter interpreter = Scripting.get(script);
            Object result = interpreter.getResultOfFunction(function, objects);
            return result instanceof String ? Quality.REGISTRY.get(new ResourceLocation((String) result)) : null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    @Optional.Method(modid = "energy")
    public static void onFoodEaten(AddFoodStatsEvent event) {
        if (event.getStack().hasTagCompound()) {
            Quality quality = QualityEvents.getQualityFromString(event.getStack().getTagCompound().getString("Quality"));
            if (quality != null) event.setNewSaturation((float) (event.getSaturation() * quality.modifier()));
        }
    }
}

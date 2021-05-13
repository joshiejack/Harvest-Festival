package uk.joshiejack.harvestcore.client.renderer.block;

import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.penguinlib.client.util.ModelCache;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = HarvestCore.MODID, value = Side.CLIENT)
public class QualityModelOverride {
    private static final Map<String, QualityModelReplaceHandler> OVERRIDES = new HashMap<>();
    public static final Map<Quality, TextureAtlasSprite> PUMPKINS = Maps.newHashMap();

    @SubscribeEvent
    public static void onStitching(TextureStitchEvent event) {
        Quality.REGISTRY.values().stream().filter(quality -> quality.modifier() != 1D)
                .forEach(quality -> {
                    PUMPKINS.put(quality, event.getMap().registerSprite(new ResourceLocation(quality.getRegistryName().getNamespace(), "blocks/pumpkin_side_" + quality.getRegistryName().getPath())));
                    ModelCache.INSTANCE.getOrLoadModel(quality.model()).getTextures().forEach(r -> event.getMap().registerSprite(r));
                });
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.DataLoaded event) {
        event.table("quality_replacements").rows().forEach(row -> {
            String item = row.get("item");
            if (!Strings.isNullOrEmpty(item)) {
                String type = row.get("type");
                switch (type) {
                    case "basic":
                        OVERRIDES.put(item, QualityModelReplaceHandler.BASIC);
                        break;
                    case "named":
                        OVERRIDES.put(item, new QualityModelReplaceHandler.QualityModelReplaceNamed(row.get("data").toString().replace("\"", "").replace(" ", "").split(",")));
                        break;
                    case "penguin":
                        OVERRIDES.put(item, QualityModelReplaceHandler.PENGUIN);
                        break;
                }
            }
        });
    }

    @SuppressWarnings("all")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBaking(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        OVERRIDES.entrySet().stream()
                .map(kv -> Pair.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(kv.getKey())), kv.getValue()))
                .filter(pair -> pair.getKey() != null)
                .forEach(pair -> pair.getRight().replace(pair.getLeft(), registry));

        //Pumpkin exception
        IBakedModel original = registry.getObject(new ModelResourceLocation(Blocks.PUMPKIN.getRegistryName(), "inventory"));
        registry.putObject(new ModelResourceLocation(Blocks.PUMPKIN.getRegistryName(), "inventory"), new QualityModel(original));
    }
}

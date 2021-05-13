package uk.joshiejack.penguinlib.data.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.io.FileUtils;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.creativetab.CustomPenguinTab;
import uk.joshiejack.penguinlib.data.custom.block.ICustomBlock;
import uk.joshiejack.penguinlib.data.custom.entity.AbstractEntityData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItem;
import uk.joshiejack.penguinlib.data.custom.material.CustomToolMaterialData;
import uk.joshiejack.penguinlib.item.ItemCustomEntitySpawner;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class CustomLoader {
    public static final Int2ObjectMap<AbstractEntityData> entities = new Int2ObjectOpenHashMap<>();
    public static final Multimap<Class<? extends Entity>, ResourceLocation> subids = HashMultimap.create();
    public static final Map<String, CustomToolMaterialData> tool_materials = Maps.newHashMap();
    public static final NonNullList<CustomPenguinTab> tabs = NonNullList.create();
    public static final Map<ResourceLocation, ResourceLocation> LOOT_OVERRIDES = Maps.newHashMap();
    public static final Multimap<ResourceLocation, ResourceLocation> LOOT_MERGES = HashMultimap.create();
    private final Set<String> mods = new HashSet<>();

    private static CustomLoader instance;

    public static void startLoading() {
        instance = new CustomLoader();
        MinecraftForge.EVENT_BUS.register(instance);
    }

    public static CustomLoader getInstance() {
        return instance;
    }

    public static void add(String modid) {
        instance.mods.add(modid);
    }

    private final List<Item> coloredItems = new ArrayList<>();
    private final List<IPenguinItem> items = new ArrayList<>();
    private final List<Block> coloredBlocks = new ArrayList<>();
    private final List<IPenguinBlock> blocks = new ArrayList<>();

    public static List<Block> getColoredBlocks() {
        return instance.coloredBlocks;
    }
    public static List<Item> getColoredItems() {
        return instance.coloredItems;
    }

    public static List<IPenguinItem> getItems() {
        return instance.items;
    }

    @SuppressWarnings("ConstantConditions")
    public final ForgeRegistry<Block> blockForgeRegistry = (Block value, IForgeRegistry<Block> registry) -> {
        registry.register(value);
        if (value instanceof IPenguinBlock && !mods.contains(value.getRegistryName().getNamespace()))
            blocks.add((IPenguinBlock) value);
        if (value instanceof ICustomBlock && hasColoring((ICustom) value))
            coloredBlocks.add(value);
    };

    public final ForgeRegistry<Item> itemForgeRegistry = (Item value, IForgeRegistry<Item> registry) -> {
        registry.register(value);
        if (value instanceof IPenguinItem)
            items.add((IPenguinItem) value);
        if (value instanceof ICustomItem)
            ((ICustomItem)value).init();
        if (value instanceof ICustomItem && hasColoring((ICustom) value))
            coloredItems.add(value);
    };

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerBlocks(final RegistryEvent.Register<Block> event) {
        ResourceLoader.loadJson(AbstractCustomObject.class, "blocks", mods).stream()
                .map(json -> (Block) CustomLoader.build(json))
                .forEach((Block block) -> blockForgeRegistry
                        .register(block, event.getRegistry()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerItems(final RegistryEvent.Register<Item> event) {
        ResourceLoader.loadJson(AbstractCustomObject.class, "items", mods).stream()
                .map(json -> (Item) CustomLoader.build(json))
                .forEach((Item item) -> itemForgeRegistry
                        .register(item, event.getRegistry()));
        blocks.forEach((IPenguinBlock block) -> itemForgeRegistry
                .register(block.createItemBlock(), event.getRegistry()));
        if (entities.size() > 0) event.getRegistry().register(new ItemCustomEntitySpawner());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerBiomes(final RegistryEvent.Register<Biome> event) {
        ResourceLoader.loadJson(AbstractCustomObject.class, "biomes", mods).stream()
                .map(json -> (Biome) CustomLoader.build(json))
                .forEach((Biome biome) -> event.getRegistry().register(biome));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerPotions(final RegistryEvent.Register<Potion> event) {
        ResourceLoader.loadJson(AbstractCustomObject.class, "potions", mods).stream()
                .map(json -> (Potion) CustomLoader.build(json))
                .forEach((Potion potion) -> event.getRegistry().register(potion));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        ResourceLoader.loadJson(AbstractCustomObject.class, "sounds", mods).stream()
                .map(json -> (SoundEvent) CustomLoader.build(json))
                .forEach((SoundEvent sound) -> event.getRegistry().register(sound));
    }

    public interface ForgeRegistry<V extends IForgeRegistryEntry<V>> {
        void register(V object, IForgeRegistry<V> registry);
    }

    private static boolean hasColoring(ICustom custom) {
        if (custom.getDefaults().color != -1) return true;
        for (AbstractCustomData.ItemOrBlock<?, ?> data : custom.getStates()) {
            if (data.color != -1) return true;
        }

        return false;
    }

    public static void finishLoading() {
        MinecraftForge.EVENT_BUS.unregister(instance);
        instance = null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T buildMulti(AbstractCustomObject.Multi c) {
        return (T) registerLootTables(AbstractCustomObject.TYPE_REGISTRY.get(c.type).build(c.registryName, c.defaults, c.data));
    }

    @SuppressWarnings("unchecked")
    private static <T> T buildSingular(AbstractCustomObject.Singular c) {
        return (T) registerLootTables(AbstractCustomObject.TYPE_REGISTRY.get(c.type).build(c.registryName, c.data, (AbstractCustomData<?,?>[]) null));
    }

    public static <T> T build(AbstractCustomObject co) {
        return registerLootTables(co instanceof AbstractCustomObject.Multi ? buildMulti((AbstractCustomObject.Multi) co) : buildSingular((AbstractCustomObject.Singular) co));
    }

    private static void registerLootTableIfExists(AbstractCustomData<?, ?> o) {
        if (o instanceof HasLoot) ((HasLoot) o).register();
    }

    private static <T> T registerLootTables(T o) {
        if (o instanceof AbstractCustomObject.Multi) {
            registerLootTableIfExists(((AbstractCustomObject.Multi) o).defaults);
            for (AbstractCustomData<?, ?> data : ((AbstractCustomObject.Multi) o).data) {
                registerLootTableIfExists(data);
            }
        } else if (o instanceof AbstractCustomObject.Singular) registerLootTableIfExists(((AbstractCustomObject.Singular) o).data);

        return o;
    }

    public static class CustomBuilderLoading extends Event {
        private final Map<String, CustomObjectBuilder> builders;

        public CustomBuilderLoading(Map<String, CustomObjectBuilder> builders) {
            this.builders = builders;
        }

        public void registerBuilder(String name, CustomObjectBuilder builder) {
            builders.put(name, builder);
        }

        public static class Pre extends CustomBuilderLoading {
            public Pre(Map<String, CustomObjectBuilder> builders) {
                super(builders);
            }
        }

        public static class Post extends CustomBuilderLoading {
            public Post(Map<String, CustomObjectBuilder> builders) {
                super(builders);
            }
        }
    }

    public interface CustomObjectBuilder {
        void build(AbstractCustomObject co);
    }

    public static void loadRecipes() {
        Map<String, CustomObjectBuilder> entries = Maps.newLinkedHashMap();
        MinecraftForge.EVENT_BUS.post(new CustomBuilderLoading.Post(entries));
        entries.forEach((directory, action) ->
                ResourceLoader.loadJson(AbstractCustomObject.class, directory).forEach(action::build));
    }

    @SuppressWarnings("unchecked")
    public static void loadObjects() {
        Map<String, CustomObjectBuilder> entries = Maps.newLinkedHashMap();
        entries.put("armor_materials", CustomLoader::build);
        entries.put("tool_materials", tm -> tool_materials.put(tm.registryName.getPath(), build(tm)));
        entries.put("entities", ce -> entities.put(build(ce), (AbstractEntityData<?>) ((AbstractCustomObject.Singular) ce).data));
        entries.put("tabs", ct -> tabs.add(build(ct)));
        entries.put("loot_tables", CustomLoader::build); //Build but do nothing with it
        MinecraftForge.EVENT_BUS.post(new CustomBuilderLoading.Pre(entries));

        entries.forEach((directory, action) ->
                ResourceLoader.loadJson(AbstractCustomObject.class, directory).forEach(action::build));

        //Register all the relevant entities
        entities.forEach((id, data) -> {
            EntityRegistry.registerModEntity(new ResourceLocation(PenguinLib.MOD_ID, data.type), data.entity, data.type, id, PenguinLib.instance, 80, 3, true);
            RenderingRegistry.registerEntityRenderingHandler(data.entity, data.renderFactory);
        });

        //Load the lang
        File folder = new File(PenguinLib.getDirectory(), "assets");
        if (!folder.exists()) folder.mkdir();
        File directory = new File(folder, "lang");
        if (!directory.exists()) directory.mkdir();
        FileUtils.listFiles(directory, new String[]{"lang"}, true)
                .forEach(file -> {
                    try (InputStream is = FileUtils.openInputStream(file)) {
                        LanguageMap.inject(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
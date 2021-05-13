package uk.joshiejack.harvestfestival;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomObject;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = HarvestFestival.MODID)
@Mod(modid = HarvestFestival.MODID, name = "Harvest Festival", version = "@HFVERSION@", dependencies = "required-after:penguinlib")
public class HarvestFestival {
    public static final String MODID = "harvestfestival";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CustomLoader.add(MODID);
    }

    private static List<IPenguinBlock> blocks = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "blocks", MODID).stream()
                .map(json -> (Block) CustomLoader.build(json))
                .forEach(block -> {
                    CustomLoader.getInstance().blockForgeRegistry
                            .register(block, event.getRegistry());
                    if (block instanceof IPenguinBlock) {
                        blocks.add((IPenguinBlock) block);
                    }
                });
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "items", MODID)
                .stream().map(json -> (Item) CustomLoader.build(json))
                .forEach(item ->
                        CustomLoader.getInstance().itemForgeRegistry
                                .register(item, event.getRegistry()));
        blocks.forEach(block ->
                CustomLoader.getInstance().itemForgeRegistry
                        .register(block.createItemBlock(), event.getRegistry()));
        blocks = null; // Remove from memory
    }

    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "biomes", MODID).stream()
                .map(json -> (Biome) CustomLoader.build(json))
                .forEach(biome -> event.getRegistry().register(biome));
    }

    @SubscribeEvent
    public static void registerPotions(final RegistryEvent.Register<Potion> event) {
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "potions", MODID).stream()
                .map(json -> (Potion) CustomLoader.build(json))
                .forEach(potion -> event.getRegistry().register(potion));
    }

    @SubscribeEvent
    public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "sounds", MODID).stream()
                .map(json -> (SoundEvent) CustomLoader.build(json))
                .forEach(sound -> event.getRegistry().register(sound));
    }
}
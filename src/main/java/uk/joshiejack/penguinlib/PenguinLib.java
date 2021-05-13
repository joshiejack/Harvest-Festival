package uk.joshiejack.penguinlib;

import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.server.command.CommandTreeBase;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.penguinlib.block.BlockInternalAir;
import uk.joshiejack.penguinlib.client.gui.GuiGreenScreen;
import uk.joshiejack.penguinlib.client.gui.book.GuiUniversalGuide;
import uk.joshiejack.penguinlib.creativetab.CustomPenguinTab;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomObject;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.inventory.ContainerBook;
import uk.joshiejack.penguinlib.inventory.ContainerGreenScreen;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;
import uk.joshiejack.penguinlib.item.custom.ItemCustomGuide;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.PlaceableHelper;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;
import uk.joshiejack.penguinlib.ticker.TickerWorldListener;
import uk.joshiejack.penguinlib.creativetab.PenguinTab;
import uk.joshiejack.penguinlib.util.helpers.minecraft.RecipeHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.penguinlib.world.storage.loot.*;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;
import static uk.joshiejack.penguinlib.item.ItemDinnerware.Dinnerware.*;


@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID)
@Mod(modid = MOD_ID, name = "PenguinLib", version = "@LIB_VERSION@")
public class PenguinLib implements IGuiHandler {
    public static final String MOD_ID = "penguinlib";
    @SidedProxy(clientSide = "uk.joshiejack.penguinlib.client.PenguinClient", serverSide = "uk.joshiejack.penguinlib.PenguinCommon")
    public static PenguinCommon proxy;
    public static Logger logger;

    @Mod.Instance(PenguinLib.MOD_ID)
    public static PenguinLib instance;

    public static final boolean IS_DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    private static File directory;
    public static boolean addGlass;
    public static boolean addPicklingJar;
    public static boolean addPlate;
    public static boolean addBowl;
    public static BlockInternalAir AIR;
    public static CreativeTabs CUSTOM_TAB = new PenguinTab(MOD_ID, () -> PenguinItems.SPECIAL.getStackFromEnum(ItemSpecial.Special.SPEECH_BUBBLE));

    @SuppressWarnings("unchecked")
    @Mod.EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        proxy.onConstruction();
    }

    private Database database;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        directory = new File(event.getModConfigurationDirectory(), MOD_ID);
        proxy.setup(event.getAsmData());
        //Load in the config overrides
        Database config = new Database();
        config.load("config_overrides");
        config.print();
        database = new Database().load("database"); //Load in the database data at preInit
        MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent.ConfigLoad(config));
        MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent.DataLoaded(database));
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, this);
        LootFunctionManager.registerFunction(new SetEnum.Serializer());
        LootFunctionManager.registerFunction(new SetString.Serializer());
        LootFunctionManager.registerFunction(new ApplyFortune.Serializer());
        LootConditionManager.registerCondition(new ConditionObtained.Serializer());
        LootConditionManager.registerCondition(new ConditionUnobtained.Serializer());
        CustomLoader.loadObjects(); //Load the shit earlier
        CustomLoader.startLoading();
    }

    @SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        event.add(AbstractCustomData.class, (d, c, s, l) -> AbstractCustomObject.TYPE_REGISTRY.put(l, c.newInstance()));
        event.add(Placeable.class, (d, c, s, l) -> PlaceableHelper.TYPE_REGISTRY.put(l, d.getClassName()));
        event.add(PenguinPacket.class, ((d, clazz, sides, loadingData) -> sides.forEach((side) -> PenguinNetwork.registerPacket(clazz, side))));
        event.add(Holder.class, ((d, c, s, l) -> c.newInstance().register()));
        event.add(TileEntity.class, ((d, c, s, l) -> GameRegistry.registerTileEntity(c, new ResourceLocation(MOD_ID, l))));
        event.add(CommandTreeBase.class, ((d, c, s, l) -> PenguinLib.TREES.add((CommandTreeBase) c.getField("INSTANCE").get(null))));
        event.add(DailyTicker.class, ((d, c, s, l) -> TickerRegistry.registerType(l, c.newInstance())));
        event.add(ContainerBook.class, (((d, c, s, l) -> ContainerBook.REGISTRY.put(Byte.parseByte(l), c))));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemRegistration(final RegistryEvent.Register<net.minecraft.item.Item> event) {
        MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent.Pre());
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Register the vanilla fish in the ore dictionary
        OreDictionary.registerOre("fish", new ItemStack(Items.FISH, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("fishCod", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));
        OreDictionary.registerOre("fishSalmon", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        OreDictionary.registerOre("fishClownfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
        OreDictionary.registerOre("fishPufferfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
        OreDictionary.registerOre("fishRaw", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));
        OreDictionary.registerOre("fishRaw", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        //Register vanilla stuff in the ore dictionary
        OreDictionary.registerOre("cropApple", new ItemStack(Items.APPLE));
        OreDictionary.registerOre("cropMelon", new ItemStack(Items.MELON));
        OreDictionary.registerOre("cropNetherwart", new ItemStack(Items.NETHER_WART));
        OreDictionary.registerOre("cropBeetroot", new ItemStack(Items.BEETROOT));
        OreDictionary.registerOre("cropPumpkin", new ItemStack(Blocks.PUMPKIN));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CustomLoader.loadRecipes();
        CustomLoader.finishLoading();
        MinecraftForge.EVENT_BUS.post(new DatabaseLoadedEvent.LoadComplete(database));
        database = null; //Clear out the database
        ResourceLoader.clear();
        if (TickerRegistry.isActive()) {
            MinecraftForge.EVENT_BUS.register(TickerWorldListener.create()); //Initialise
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        //Load the custom tabs with their actual values
        CustomLoader.tabs.forEach(CustomPenguinTab::init); //Load the tabs after everything
        if (PenguinConfig.addDishRecipes) {
            RecipeHelper helper = new RecipeHelper(event.getRegistry(), MOD_ID);
            if (addPlate) GameRegistry.addSmelting(PenguinItems.DINNERWARE.getStackFromEnum(PLATE_UNFIRED), PenguinItems.DINNERWARE.getStackFromEnum(PLATE_FIRED), 0.2F);
            if (addGlass) helper.shapedRecipe("glass", PenguinItems.DINNERWARE.getStackFromEnum(GLASS, 4), "G G", " G ", 'G', "paneGlassColorless");
            if (addPicklingJar) helper.shapedRecipe("pickling_jar", PenguinItems.DINNERWARE.getStackFromEnum(PICKLING_JAR, 4), "S ", "G ", "G ", 'G', "blockGlassColorless", 'S', "slabWood");
            if (addPlate) helper.shapedRecipe("plate", PenguinItems.DINNERWARE.getStackFromEnum(PLATE_UNFIRED, 2), "CC", 'C', Items.CLAY_BALL);
            if (addBowl) helper.shapedRecipe("bowl", PenguinItems.DINNERWARE.getStackFromEnum(BOWL, 6), "P P", "SSS", 'P', "plankWood", 'S', "slabWood");
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.END && event.world.getWorldTime() % TimeHelper.TICKS_PER_DAY == 1) {
            MinecraftForge.EVENT_BUS.post(new NewDayEvent((WorldServer) event.world)); //Post the new day event, to update
        }
    }

    private static final List<CommandTreeBase> TREES = Lists.newArrayList();

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        TREES.forEach(event::registerServerCommand);
    }

    public static File getDirectory() {
        if (directory != null && !directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    public static File getCustomFolder() {
        File folder = new File(getDirectory(), "custom");
        if (!folder.exists()) folder.mkdir();
        return folder;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1) {
            return new ContainerGreenScreen(player.inventory);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1) return new GuiGreenScreen(new ContainerGreenScreen(player.inventory));
        else {
            EnumHand hand = x == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
            ItemStack held = player.getHeldItem(hand);
            if (held.getItem() instanceof ItemCustomGuide) {
                return GuiUniversalGuide.getGui((ItemCustomGuide) held.getItem());
            }
        }

        return null;
    }
}

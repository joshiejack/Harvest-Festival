package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.AnimalEvents;
import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarRender;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.commands.HFCommandDay;
import joshie.harvest.core.commands.HFCommandGold;
import joshie.harvest.core.commands.HFCommandHelp;
import joshie.harvest.core.commands.HFCommandNewDay;
import joshie.harvest.core.commands.HFCommandSeason;
import joshie.harvest.core.commands.HFCommandWeather;
import joshie.harvest.core.commands.HFCommandYear;
import joshie.harvest.core.handlers.EventsHandler;
import joshie.harvest.core.handlers.GoddessHandler;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.network.PacketCropRequest;
import joshie.harvest.core.network.PacketDismount;
import joshie.harvest.core.network.PacketFreeze;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketPurchaseItem;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.network.PacketSyncBirthday;
import joshie.harvest.core.network.PacketSyncCooking;
import joshie.harvest.core.network.PacketSyncCrop;
import joshie.harvest.core.network.PacketSyncForecast;
import joshie.harvest.core.network.PacketSyncFridge;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.core.network.PacketSyncMarker;
import joshie.harvest.core.network.PacketSyncMarriage;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.network.PacketSyncRelationship;
import joshie.harvest.core.network.PacketSyncStats;
import joshie.harvest.core.network.PacketWateringCan;
import joshie.harvest.core.network.animals.PacketSyncDaysNotFed;
import joshie.harvest.core.network.animals.PacketSyncEverything;
import joshie.harvest.core.network.animals.PacketSyncHealthiness;
import joshie.harvest.core.network.animals.PacketSyncProductsProduced;
import joshie.harvest.core.network.quests.PacketQuestCompleted;
import joshie.harvest.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvest.core.network.quests.PacketQuestSetAvailable;
import joshie.harvest.core.network.quests.PacketQuestSetCurrent;
import joshie.harvest.core.network.quests.PacketQuestSetStage;
import joshie.harvest.core.network.quests.PacketQuestStart;
import joshie.harvest.core.render.RenderHandler;
import joshie.harvest.core.util.WorldDestroyer;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.player.relationships.RelationshipHelper;
import joshie.harvest.quests.QuestEvents;
import joshie.harvest.quests.QuestRegistry;
import joshie.harvest.shops.ShopRegistry;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HFCore {
    public static void preInit() {
        //Register API Handlers
        HFApi.ANIMALS = new AnimalRegistry();
        HFApi.CALENDAR = new CalendarHelper();
        HFApi.CROPS = new CropRegistry();
        HFApi.COOKING = new FoodRegistry();
        HFApi.NPC = new NPCRegistry();
        HFApi.SHOPS = new ShopRegistry();
        HFApi.QUESTS = new QuestRegistry();
        HFApi.RELATIONS = new RelationshipHelper();

        //Register Events
        FMLCommonHandler.instance().bus().register(new EventsHandler());
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new GoddessHandler());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());
        
        //Commands
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        CommandManager.INSTANCE.registerCommand(new HFCommandHelp());
        CommandManager.INSTANCE.registerCommand(new HFCommandGold());
        CommandManager.INSTANCE.registerCommand(new HFCommandSeason());
        CommandManager.INSTANCE.registerCommand(new HFCommandDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandYear());
        CommandManager.INSTANCE.registerCommand(new HFCommandNewDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandWeather());

        //Register Packets
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncForecast.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncRelationship.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncMarriage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncStats.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCooking.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncOrientation.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncBirthday.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketPurchaseItem.class, Side.SERVER);
        PacketHandler.registerPacket(PacketFreeze.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncMarker.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncFridge.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketWateringCan.class, Side.SERVER);
        PacketHandler.registerPacket(PacketDismount.class, Side.SERVER);
        
        //Animal Packets
        PacketHandler.registerPacket(PacketSyncEverything.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncHealthiness.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncDaysNotFed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncProductsProduced.class, Side.CLIENT);

        //Quest Packets
        PacketHandler.registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestStart.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.SERVER);
    }

    public static void postInit() {
        WorldDestroyer.replaceWorldProvider();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        HFTrackers.reset(null);
        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.CROPS = RenderingRegistry.getNextAvailableRenderId();
        RenderIds.COOKING = RenderingRegistry.getNextAvailableRenderId();
        MinecraftForge.EVENT_BUS.register(new CalendarRender());
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
        FMLCommonHandler.instance().bus().register(new RenderHandler());
    }
}

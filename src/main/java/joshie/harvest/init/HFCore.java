package joshie.harvest.init;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.AnimalEvents;
import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.CropRegistry;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarRender;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.handlers.events.FMLEvents;
import joshie.harvest.core.handlers.events.GeneralEvents;
import joshie.harvest.core.handlers.events.ToolLevelRender;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.network.PacketCropRequest;
import joshie.harvest.core.network.PacketDismount;
import joshie.harvest.core.network.PacketFreeze;
import joshie.harvest.core.network.PacketGoldCommand;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketNewDay;
import joshie.harvest.core.network.PacketPurchaseItem;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.network.PacketSetWeather;
import joshie.harvest.core.network.PacketSyncBirthday;
import joshie.harvest.core.network.PacketSyncCanProduce;
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
import joshie.harvest.core.network.quests.PacketQuestCompleted;
import joshie.harvest.core.network.quests.PacketQuestDecreaseHeld;
import joshie.harvest.core.network.quests.PacketQuestSetAvailable;
import joshie.harvest.core.network.quests.PacketQuestSetCurrent;
import joshie.harvest.core.network.quests.PacketQuestSetStage;
import joshie.harvest.core.network.quests.PacketQuestStart;
import joshie.harvest.core.util.WorldDestroyer;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.quests.QuestEvents;
import joshie.harvest.quests.QuestRegistry;
import joshie.harvest.relations.RelationshipHelper;
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
        FMLCommonHandler.instance().bus().register(new FMLEvents());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new HFCommands());
        MinecraftForge.EVENT_BUS.register(new GeneralEvents());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());

        //Register Packets
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetWeather.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncForecast.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncRelationship.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncMarriage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncStats.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCooking.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncOrientation.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncBirthday.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketPurchaseItem.class, Side.SERVER);
        PacketHandler.registerPacket(PacketGoldCommand.class, Side.SERVER);
        PacketHandler.registerPacket(PacketFreeze.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncMarker.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncFridge.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketNewDay.class, Side.SERVER);
        PacketHandler.registerPacket(PacketWateringCan.class, Side.SERVER);
        PacketHandler.registerPacket(PacketDismount.class, Side.SERVER);

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
        FMLCommonHandler.instance().bus().register(new ToolLevelRender());
    }
}

package joshie.harvestmoon;

import static joshie.harvestmoon.lib.HMModInfo.MODID;
import static joshie.harvestmoon.lib.HMModInfo.MODNAME;
import static joshie.harvestmoon.lib.HMModInfo.MODPATH;

import java.io.File;

import joshie.harvestmoon.buildings.HMBuildings;
import joshie.harvestmoon.commands.CommandHandler;
import joshie.harvestmoon.handlers.CommonHandler;
import joshie.harvestmoon.handlers.GuiHandler;
import joshie.harvestmoon.handlers.events.AnimalEvents;
import joshie.harvestmoon.handlers.events.FMLEvents;
import joshie.harvestmoon.handlers.events.GeneralEvents;
import joshie.harvestmoon.handlers.events.QuestEvents;
import joshie.harvestmoon.handlers.events.ToolEvents;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMCooking;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMEntities;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.network.PacketCropRequest;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import joshie.harvestmoon.network.PacketSyncCanProduce;
import joshie.harvestmoon.network.PacketSyncCooking;
import joshie.harvestmoon.network.PacketSyncCrop;
import joshie.harvestmoon.network.PacketSyncGold;
import joshie.harvestmoon.network.PacketSyncOrientation;
import joshie.harvestmoon.network.PacketSyncRelations;
import joshie.harvestmoon.network.PacketSyncStats;
import joshie.harvestmoon.network.quests.PacketQuestCompleted;
import joshie.harvestmoon.network.quests.PacketQuestDecreaseHeld;
import joshie.harvestmoon.network.quests.PacketQuestSetAvailable;
import joshie.harvestmoon.network.quests.PacketQuestSetCurrent;
import joshie.harvestmoon.network.quests.PacketQuestSetStage;
import joshie.harvestmoon.network.quests.PacketQuestStart;
import joshie.harvestmoon.util.WorldDestroyer;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MODID, name = MODNAME)
public class HarvestMoon {
    public static CommonHandler handler = new CommonHandler();

    @SidedProxy(clientSide = "joshie.harvestmoon.ClientProxy", serverSide = "joshie.harvestmoon.CommonProxy")
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MODNAME);
    
    @Instance(MODID)
    public static HarvestMoon instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + File.separator + MODPATH);
        HMConfiguration.init();
        HMBuildings.init();
        HMCrops.init();
        HMNPCs.init();
        HMBlocks.init();
        HMItems.init();
        HMCooking.init();
        HMEntities.init();
        HMQuests.init();

        proxy.init();
        FMLCommonHandler.instance().bus().register(new FMLEvents());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new CommandHandler());
        MinecraftForge.EVENT_BUS.register(new GeneralEvents());
        MinecraftForge.EVENT_BUS.register(new QuestEvents());
        MinecraftForge.EVENT_BUS.register(new ToolEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        return;
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PacketHandler.registerPacket(PacketCropRequest.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSetCalendar.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncCrop.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCanProduce.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncRelations.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncRelations.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncStats.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCooking.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncOrientation.class, Side.CLIENT);

        //Quest Packets
        PacketHandler.registerPacket(PacketQuestSetAvailable.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetCurrent.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestCompleted.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestStart.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestDecreaseHeld.class, Side.SERVER);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketQuestSetStage.class, Side.SERVER);
        WorldDestroyer.replaceWorldProvider();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        CommandHandler.init(event.getServer().getCommandManager());
    }
}

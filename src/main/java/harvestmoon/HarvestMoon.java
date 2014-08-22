package harvestmoon;

import static harvestmoon.lib.ModInfo.MODID;
import static harvestmoon.lib.ModInfo.MODNAME;
import harvestmoon.buildings.HMBuildings;
import harvestmoon.commands.CommandHandler;
import harvestmoon.handlers.CommonHandler;
import harvestmoon.handlers.GuiHandler;
import harvestmoon.handlers.events.AnimalEvents;
import harvestmoon.handlers.events.FMLEvents;
import harvestmoon.handlers.events.GeneralEvents;
import harvestmoon.handlers.events.ToolEvents;
import harvestmoon.init.HMBlocks;
import harvestmoon.init.HMCooking;
import harvestmoon.init.HMCrops;
import harvestmoon.init.HMItems;
import harvestmoon.network.PacketCropRequest;
import harvestmoon.network.PacketHandler;
import harvestmoon.network.PacketSetCalendar;
import harvestmoon.network.PacketSyncCanProduce;
import harvestmoon.network.PacketSyncCrop;
import harvestmoon.network.PacketSyncGold;
import harvestmoon.network.PacketSyncRelations;
import harvestmoon.network.PacketSyncStats;
import harvestmoon.util.WorldDestroyer;
import net.minecraftforge.common.MinecraftForge;
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

    @SidedProxy(clientSide = "harvestmoon.ClientProxy", serverSide = "harvestmoon.CommonProxy")
    public static CommonProxy proxy;

    @Instance(MODID)
    public static HarvestMoon instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        HMBuildings.init();
        HMCrops.init();
        HMBlocks.init();
        HMItems.init();
        HMCooking.init();

        proxy.init();
        FMLCommonHandler.instance().bus().register(new FMLEvents());
        MinecraftForge.EVENT_BUS.register(new AnimalEvents());
        MinecraftForge.EVENT_BUS.register(new CommandHandler());
        MinecraftForge.EVENT_BUS.register(new GeneralEvents());
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
        WorldDestroyer.replaceWorldProvider();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        CommandHandler.init(event.getServer().getCommandManager());
    }
}

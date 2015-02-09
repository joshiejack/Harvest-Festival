package joshie.harvestmoon;

import static joshie.harvestmoon.lib.HMModInfo.MODID;
import static joshie.harvestmoon.lib.HMModInfo.MODNAME;
import static joshie.harvestmoon.lib.HMModInfo.MODPATH;

import java.io.File;

import joshie.harvestmoon.handlers.CommonHandler;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.init.HMCommands;
import joshie.harvestmoon.init.HMCooking;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMEntities;
import joshie.harvestmoon.init.HMHandlers;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMPackets;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.util.WorldDestroyer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = MODNAME)
public class HarvestMoon {
    public static CommonHandler handler = new CommonHandler();

    @SidedProxy(clientSide = "joshie.harvestmoon.HMClientProxy", serverSide = "joshie.harvestmoon.HMCommonProxy")
    public static HMCommonProxy proxy;

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
        HMPackets.init();
        HMHandlers.init();
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WorldDestroyer.replaceWorldProvider();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        HMCommands.init(event.getServer().getCommandManager());
    }
}

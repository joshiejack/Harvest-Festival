package joshie.harvestmoon;

import static joshie.harvestmoon.lib.HMModInfo.MODID;
import static joshie.harvestmoon.lib.HMModInfo.MODNAME;
import static joshie.harvestmoon.lib.HMModInfo.MODPATH;

import java.io.File;
import java.util.Map;

import joshie.harvestmoon.init.HMAnimals;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMBuildings;
import joshie.harvestmoon.init.HMCommands;
import joshie.harvestmoon.init.HMCooking;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.init.HMEntities;
import joshie.harvestmoon.init.HMGifts;
import joshie.harvestmoon.init.HMHandlers;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.init.HMMining;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.init.HMPackets;
import joshie.harvestmoon.init.HMQuests;
import joshie.harvestmoon.init.HMRecipeFixes;
import joshie.harvestmoon.init.HMShops;
import joshie.harvestmoon.util.WorldDestroyer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@Mod(modid = MODID, name = MODNAME)
public class HarvestMoon implements IFMLLoadingPlugin {
    @SidedProxy(clientSide = "joshie.harvestmoon.HMClientProxy", serverSide = "joshie.harvestmoon.HMCommonProxy")
    public static HMCommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MODNAME);

    @Instance(MODID)
    public static HarvestMoon instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + File.separator + MODPATH);
        HMOverride.init();
        HMConfiguration.init();
        HMCrops.init();
        HMNPCs.init();
        HMBlocks.init();
        HMBuildings.init();
        HMItems.init();
        HMCooking.init();
        HMEntities.init();
        HMQuests.init();
        HMPackets.init();
        HMHandlers.init();
        HMShops.init();
        HMMining.init();
        HMGifts.init();
        HMAnimals.init();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        WorldDestroyer.replaceWorldProvider();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        HMRecipeFixes.init();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        HMCommands.init(event.getServer().getCommandManager());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { HMOverride.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        HMOverride.isObfuscated = ((Boolean) data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

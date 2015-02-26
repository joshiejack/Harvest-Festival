package joshie.harvestmoon;

import static joshie.harvestmoon.core.lib.HMModInfo.JAVAPATH;
import static joshie.harvestmoon.core.lib.HMModInfo.MODID;
import static joshie.harvestmoon.core.lib.HMModInfo.MODNAME;
import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;
import static joshie.harvestmoon.core.lib.HMModInfo.VERSION;

import java.io.File;
import java.util.Map;

import joshie.harvestmoon.core.HMCommonProxy;
import joshie.harvestmoon.core.util.WorldDestroyer;
import joshie.harvestmoon.init.HMCommands;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMOverride;
import joshie.harvestmoon.init.HMRecipeFixes;

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

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class HarvestMoon implements IFMLLoadingPlugin {
    @SidedProxy(clientSide = JAVAPATH + "core.HMClientProxy", serverSide = JAVAPATH + "core.HMCommonProxy")
    public static HMCommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MODNAME);

    @Instance(MODID)
    public static HarvestMoon instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + File.separator + MODPATH);
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
        HMConfiguration.clear();
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

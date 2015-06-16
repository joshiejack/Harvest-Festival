package joshie.harvest;

import static joshie.harvest.core.lib.HFModInfo.JAVAPATH;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;
import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import static joshie.harvest.core.lib.HFModInfo.VERSION;

import java.io.File;

import joshie.harvest.core.HFCommonProxy;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.init.HFRecipeFixes;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.DimensionManager;

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
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class HarvestFestival {
    @SidedProxy(clientSide = JAVAPATH + "core.HFClientProxy", serverSide = JAVAPATH + "core.HFCommonProxy")
    public static HFCommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MODNAME);

    @Instance(MODID)
    public static HarvestFestival instance;
    public static File root;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory() + File.separator + MODPATH);
        proxy.load("preInit");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.load("init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.load("postInit");
        proxy.load("initClient");
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.load("complete");
        HFRecipeFixes.complete();
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ((ServerCommandManager) manager).registerCommand(CommandManager.INSTANCE);
        }
    }
    
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        DataHelper.reset(DimensionManager.getWorld(0));
    }
}

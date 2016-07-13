package joshie.harvest;

import joshie.harvest.core.HFApiLoader;
import joshie.harvest.core.HFCommonProxy;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static joshie.harvest.core.lib.HFModInfo.*;

@Mod(modid = MODID, name = MODNAME, version = VERSION)
public class HarvestFestival {
    @SidedProxy(clientSide = JAVAPATH + "core.HFClientProxy", serverSide = JAVAPATH + "core.HFCommonProxy")
    public static HFCommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    @Instance(MODID)
    public static HarvestFestival instance;
    public static File root;

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        HFApiLoader.init(); //Load in the api once construction is done
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        root = new File(event.getModConfigurationDirectory(), MODID);
        proxy.load("preInit");
        HFApiLoader.load(event.getAsmData());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.load("init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.load("postInit");
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        proxy.load("complete");
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        ICommandManager manager = event.getServer().getCommandManager();
        if (manager instanceof ServerCommandManager) {
            ((ServerCommandManager) manager).registerCommand(CommandManager.INSTANCE);
        }

        HFTrackers.resetServer();
    }
}
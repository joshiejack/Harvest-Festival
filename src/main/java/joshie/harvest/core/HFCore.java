package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.calendar.CalendarHUD;
import joshie.harvest.calendar.CalendarRender;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.commands.*;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.network.*;
import joshie.harvest.core.network.animals.PacketSyncDaysNotFed;
import joshie.harvest.core.network.animals.PacketSyncEverything;
import joshie.harvest.core.network.animals.PacketSyncHealthiness;
import joshie.harvest.core.network.animals.PacketSyncProductsProduced;
import joshie.harvest.core.render.RenderHandler;
import joshie.harvest.core.util.WorldDestroyer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class HFCore {
     public static void preInit() {
        WorldDestroyer.replaceWorldProvider();
        NetworkRegistry.INSTANCE.registerGuiHandler(HarvestFestival.instance, new GuiHandler());

        //Commands
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        CommandManager.INSTANCE.registerCommand(new HFCommandHelp());
        CommandManager.INSTANCE.registerCommand(new HFCommandGold());
        CommandManager.INSTANCE.registerCommand(new HFCommandSeason());
        CommandManager.INSTANCE.registerCommand(new HFCommandDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandYear());
        CommandManager.INSTANCE.registerCommand(new HFCommandNewDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandTime());
        CommandManager.INSTANCE.registerCommand(new HFCommandWeather());
        CommandManager.INSTANCE.registerCommand(new HFCommandMine());

        //Register Packets
        PacketHandler.registerPacket(PacketSetCalendar.class);
        PacketHandler.registerPacket(PacketSyncForecast.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGold.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncRelationship.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncMarriage.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncGifted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncBirthday.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketPurchaseItem.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncFridge.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketWateringCan.class, Side.SERVER);
        PacketHandler.registerPacket(PacketDismount.class, Side.SERVER);
        PacketHandler.registerPacket(PacketRenderUpdate.class, Side.CLIENT);

        //Animal Packets
        PacketHandler.registerPacket(PacketSyncEverything.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncHealthiness.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncDaysNotFed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncProductsProduced.class, Side.CLIENT);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        if (HFCalendar.ENABLE_DATE_HUD || HFCalendar.ENABLE_GOLD_HUD) MinecraftForge.EVENT_BUS.register(new CalendarHUD());
        MinecraftForge.EVENT_BUS.register(new CalendarRender());
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
    }

    //Configure
    public static boolean DEBUG_MODE = true;
    public static int MINING_ID;
    public static int OVERWORLD_ID;

    public static void configure () {
        OVERWORLD_ID = getInteger("Overworld ID", 3);
        MINING_ID = getInteger("Mining World ID", 4);
    }
}

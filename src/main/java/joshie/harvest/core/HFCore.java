package joshie.harvest.core;

import joshie.harvest.HarvestFestival;
import joshie.harvest.calendar.CalendarHUD;
import joshie.harvest.calendar.CalendarRender;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.handlers.GuiHandler;
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

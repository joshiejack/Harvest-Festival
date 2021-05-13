package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.TimeChangedEvent;

import java.time.DayOfWeek;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class TimeHelper {
    public static long TICKS_PER_DAY = 24000L;
    public static double SCALE = TICKS_PER_DAY / 24000D;
    public static long SIX_AM = (long) (SCALE * 6000D);
    public static final DayOfWeek[] DAYS = DayOfWeek.values();

    public static long scaleTime(long time) {
        return (long) (SCALE * (double) time);
    }

    public static int getElapsedDays(long time) {
        return (int) (time / TICKS_PER_DAY);
    }

    public static int getElapsedDays(World world) {
        return (int) (world.getWorldTime() / TICKS_PER_DAY);
    }

    public static long getTimeOfDay(long time) {
        return (time + SIX_AM) % TICKS_PER_DAY;
    }

    public static boolean isBetween(World world, int open, int close) {
        long timeOfDay = getTimeOfDay(world.getWorldTime()); //0-23999 by default
        return timeOfDay >= open && timeOfDay <= close;
    }

    public static DayOfWeek getWeekday(long time) {
        int days = TimeHelper.getElapsedDays(time);
        int modulus = days % 7;
        if (modulus < 0) modulus = 0;
        return DAYS[modulus];
    }

    //Post an event when the time changes to update shit
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCommand(CommandEvent event) throws NumberInvalidException {
        if (event.getCommand().getName().equalsIgnoreCase("time")) {
            String[] args = event.getParameters();
            long time = event.getSender().getEntityWorld().getWorldTime();
            if (args.length > 1) {
                switch (args[1]) {
                    case "day":
                        time = 1000;
                        break;
                    case "night":
                        time = 13000;
                        break;
                    default:
                        time = CommandBase.parseInt(args[1], 0);
                        break;
                }
            }

            if ("add".equals(args[0])) {
                int l = CommandBase.parseInt(args[1], 0);
                time = event.getSender().getEntityWorld().getWorldTime() + l;
            }

            MinecraftForge.EVENT_BUS.post(new TimeChangedEvent(event.getSender().getEntityWorld(), time));
        }
    }

    public static String shortName(DayOfWeek day) {
        return day.name().substring(0, 3);
    }
}

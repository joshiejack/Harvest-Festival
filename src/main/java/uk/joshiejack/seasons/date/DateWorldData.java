package uk.joshiejack.seasons.date;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.events.TimeChangedEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.seasons.network.PacketSyncDate;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class DateWorldData extends WorldSavedData {
    private static final String DATA_NAME = "Date-Data";
    private static DateWorldData instance;
    private final CalendarDate date = new CalendarDate();

    public DateWorldData() { super(DATA_NAME);}
    public DateWorldData(String name) {
        super(name);
    }

    @SubscribeEvent
    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        PenguinNetwork.sendToClient(new PacketSyncDate(get(event.player.world)), event.player);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onNewDay(NewDayEvent event) {
        get(event.getWorld()).set(CalendarDate.getFromTime(event.getWorld().getWorldTime()));
        instance.markDirty();
        PenguinNetwork.sendToEveryone(new PacketSyncDate(get(event.getWorld())));
    }

    @SubscribeEvent
    public static void onTimeChanged(TimeChangedEvent event) {
        PenguinNetwork.sendToEveryone(new PacketSyncDate(CalendarDate.getFromTime(event.getTime())));
    }

    public static CalendarDate get(World world) {
        instance = (DateWorldData) world.loadData(DateWorldData.class, DATA_NAME);
        if (instance == null) {
            instance = new DateWorldData(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance.date;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        return date.serializeNBT();
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        date.deserializeNBT(tag);
    }
}

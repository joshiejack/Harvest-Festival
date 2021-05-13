package uk.joshiejack.harvestcore.world.storage;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import uk.joshiejack.harvestcore.HCConfig;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.network.PacketSyncPlayerData;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.dimension.MineData;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class SavedData extends WorldSavedData {
    private static final String DATA_NAME = "HC-Data";
    private final Map<UUID, Mailroom> mailrooms = Maps.newHashMap();
    private final Int2ObjectMap<WildernessData> wilderness = new Int2ObjectOpenHashMap<>(); //Dimension > Wilderness Data
    private final Int2ObjectMap<MineData> mine = new Int2ObjectOpenHashMap<>(); //Dimension > Mine Data

    public SavedData() { super(DATA_NAME);}
    public SavedData(String name) {
        super(name);
    }

    private static SavedData get(World world) {
        SavedData instance = (SavedData) world.loadData(SavedData.class, DATA_NAME);
        if (instance == null) {
            instance = new SavedData(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance;
    }

    @SubscribeEvent
    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
        getMailroom(event.player.world, PlayerHelper.getUUIDForPlayer(event.player)).synchronize(event.player.world); //Synchro the mail to the player
        PenguinNetwork.sendToClient(new PacketSyncPlayerData(event.player, "Blueprints"), event.player);
        PenguinNetwork.sendToClient(new PacketSyncPlayerData(event.player, "Notes"), event.player);
        PenguinNetwork.sendToClient(new PacketSyncPlayerData(event.player, "NotesRead"), event.player);
    }

    public static MineData getMineDataFromName(World world, String name) {
        return getMineData(world, Mine.BY_NAME.getInt(name));
    }

    public static MineData getMineData(World world, int dimension) {
        SavedData data = SavedData.get(world);
        if (!data.mine.containsKey(dimension)) {
            data.mine.put(dimension, new MineData());
            data.markDirty();
        }

        return data.mine.get(dimension);
    }

    public static WildernessData getWildernessDataForDimension(World world, int dim) {
        return SavedData.get(world).getWildernessData(dim);
    }

    private WildernessData getWildernessData(int dim) {
        if (!wilderness.containsKey(dim)) {
            wilderness.put(dim, new WildernessData());
            markDirty(); //Save the changes
        }

        return wilderness.get(dim);
    }

    @SubscribeEvent
    public static void onNewDay(NewDayEvent event) {
        SavedData data = get(event.getWorld());
        data.mailrooms.values().forEach((m) -> m.onNewDay(event.getWorld()));
        if (HCConfig.enableWildernessSpawns) {
            data.getWildernessData(event.getWorld().provider.getDimension()).onNewDay(event.getWorld());
        }

        data.markDirty();
    }

    public static Mailroom getMailroom(World world, UUID uuid) {
        SavedData instance = get(world);
        if (!instance.mailrooms.containsKey(uuid)) {
            instance.mailrooms.put(uuid, new Mailroom(uuid));
        }

        return instance.mailrooms.get(uuid);
    }

    public static Collection<Mailroom> getMailrooms(World world) {
        return get(world).mailrooms.values();
    }

    public static void save(World world) {
        get(world).markDirty();
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        tag.setTag("Mailrooms", NBTHelper.serialize(mailrooms));
        tag.setTag("Wilderness", NBTHelper.writeDimensionMap(wilderness));
        tag.setTag("Mines", NBTHelper.writeDimensionMap(mine));
        return tag;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        NBTHelper.deserialize(this, Mailroom.class, tag.getTagList("Mailrooms", 10), mailrooms);
        NBTHelper.readDimensionMap(wilderness, tag.getTagList("Wilderness", 10), WildernessData.class);
        NBTHelper.readDimensionMap(mine, tag.getTagList("Mines", 10), MineData.class);
    }
}

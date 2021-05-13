package uk.joshiejack.penguinlib.scripting;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.data.holder.HolderRegistryList;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.scripting.wrappers.TeamJS;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class DataScripting {
    private static final Map<String, HolderRegistryList> LISTS = Maps.newHashMap();

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("registries").where("type=list").forEach(list -> {
            String name = list.name();
            LISTS.put(name, new HolderRegistryList());
            event.table(name + "_list").rows().forEach(entry -> LISTS.get(name).add(entry.holder()));
        });
    }

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("data", DataScripting.class);
    }

    public static boolean isInList(String list, ItemStack stack) {
        return LISTS.containsKey(list) && LISTS.get(list).contains(stack);
    }

    public static boolean isInList(String list, ItemStackJS item) {
        return LISTS.containsKey(list) && LISTS.get(list).contains(item.penguinScriptingObject);
    }

    public static void obtain(WorldJS<?> worldJS, TeamJS teamWrapper, String listName, ItemStackJS item) {
        PenguinTeam team = teamWrapper.penguinScriptingObject;
        ItemStack stack = item.penguinScriptingObject;
        NBTTagCompound data = team.getData(); //Get the team data
        if (!data.hasKey(listName)) data.setTag(listName, new NBTTagCompound());
        NBTTagCompound obtained = data.getCompoundTag(listName); //Get the obtained item data
        HolderMeta key = new HolderMeta(stack);
        if (!obtained.hasKey(key.toString())) {
            obtained.setBoolean(key.toString(), true); //Add it as obtained
            PenguinTeams.get(worldJS.penguinScriptingObject).markDirty(); //Save the data
            team.syncToTeam(worldJS.penguinScriptingObject); //Sync the data to the client
        }
    }

    public static void obtain(PlayerJS playerWrapper, String listName, ItemStackJS item) {
        obtain(playerWrapper.world(), playerWrapper.team(), listName,item);
    }

    public static boolean obtained(TeamJS teamWrapper, String listName, ItemStackJS item) {
        return obtained(teamWrapper, listName, new HolderMeta(item.penguinScriptingObject).toString());
    }

    public static boolean obtained(PlayerJS playerWrapper, String listName, ItemStackJS item) {
        return obtained(playerWrapper.team(), listName, item);
    }

    public static boolean obtained(TeamJS teamWrapper, String listName, String item) {
        Holder holder = Objects.requireNonNull(Holder.getFromString(item));
        NBTTagCompound data = teamWrapper.penguinScriptingObject.getData(); //Get the team data
        if (!data.hasKey(listName)) return false; //Error ahead of time
        NBTTagCompound obtained = data.getCompoundTag(listName); //Get the obtained item data
        return obtained.hasKey(holder.toString());
    }

    public static boolean obtained(PlayerJS playerWrapper, String listName, String item) {
        return obtained(playerWrapper.team(), listName, item);
    }
}

package uk.joshiejack.settlements.quest.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.settings.Repeat;
import uk.joshiejack.settlements.util.QuestHelper;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.helpers.generic.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestTracker implements INBTSerializable<NBTTagCompound> {
    private final Object2IntMap<ResourceLocation> completed = new Object2IntOpenHashMap<>(); //Save this
    private final Object2IntMap<ResourceLocation> completedDay = new Object2IntOpenHashMap<>(); //Save this
    private final Map<ResourceLocation, QuestData> active = Maps.newHashMap(); //Save this
    private static final Random rand = new Random();
    private final PenguinGroup type;
    private Quest daily;

    //List of all the active triggers
    private final Multimap<String, Quest> triggers = HashMultimap.create();
    //List of all active methods, aka for quests that aren't active
    private final Multimap<String, QuestData> methods = HashMultimap.create();

    public QuestTracker(PenguinGroup type) {
        this.type = type;
        this.addDefault(type);
    }

    private void addDefault(PenguinGroup type) {
        List<Quest> scripts = Quest.REGISTRY.values().stream().filter((script -> script.getSettings().getType() == type)).collect(Collectors.toList());
        for (Quest script: scripts) {
            if (script.getSettings().isDefault() && !completed.containsKey(script.getRegistryName())) {
                active.put(script.getRegistryName(), new QuestData(script));
            }
        }
    }

    public Collection<QuestData> getActive(String method) {
        return methods.get(method);
    }

    @SuppressWarnings("ConstantConditions")
    public void onNewDay(@Nullable WorldServer world) {
        List<Quest> dailies = Quest.REGISTRY.values().stream().filter(q -> q.getSettings().getType() == type && q.getSettings().isDaily()
                && !active.containsKey(q.getRegistryName())).collect(Collectors.toList());
        if (dailies.size() > 0) {
            daily = dailies.get((world == null ? rand: world.rand).nextInt(dailies.size()));
            daily.getInterpreter().callFunction("onTaskCreation");
        } else daily = null;
    }

    @Nullable
    public Quest getDaily() {
        return daily;
    }

    public QuestData getData(ResourceLocation quest) {
        return active.get(quest);
    }

    public boolean hasCompleted(ResourceLocation questID) {
        return completed.containsKey(questID);
    }

    public boolean hasCompleted(ResourceLocation questID, int amount) {
        return hasCompleted(questID) && completed.getInt(questID) >= amount;
    }

    public void reload(int day) {
        onNewDay(null); //Reset the selected daily quest
        for (Map.Entry<ResourceLocation, QuestData> entry: Sets.newHashSet(active.entrySet())) {
            NBTTagCompound tag = entry.getValue().serializeNBT(); //Save the old data
            QuestData nD = new QuestData(Quest.REGISTRY.get(entry.getKey()));
            nD.deserializeNBT(tag); //Copy in the new data
            active.put(entry.getKey(), nD);
        }

        //Reload shit
        setupOrRefresh(day);
    }

    public void setupOrRefresh(int day) {
        triggers.clear();
        methods.clear();
        //Build a list of all quests that are not currently active, but that are able to be started i.e. completed < repeat amount
        for (Quest quest: Quest.REGISTRY.values()) {
            if (quest.getSettings().getType() != type) continue;
            if (completed.containsKey(quest.getRegistryName())) {
                if (quest.getSettings().getRepeat() == Repeat.NONE) continue; //If we have completed the quest and we don't repeat again, skip this
                else {
                    int lastCompletion = completedDay.getInt(completedDay);
                    int daysBetween = day - lastCompletion;
                    if (daysBetween < quest.getSettings().getRepeat().getDays()) continue; //We can't readd so don't
                }
            }

            //Now we know that things can't be added to the triggers, we now need to check if the quest is active
            if (active.containsKey(quest.getRegistryName())) {
                //Ok we found a match so we don't want to add "ActiveTriggers" for "ActiveMethods"
                Scripting.getMethods().stream().filter(m -> quest.getInterpreter().hasMethod(m)).forEach((method) -> methods.get(method).add(active.get(quest.getRegistryName())));
            } else {
                //We know the quest isn't currently active, and in theory it can be started as we are just ignoring the prereqs, and checking in the canStartMethod instead
                //Therefore we want to call the canStart method/other methods where applicable so let's add those to the triggers
                quest.getTriggers().forEach((m) -> triggers.get(m).add(quest));
            }
        }
    }

    public void start(Quest original) {
        active.put(original.getRegistryName(), new QuestData(original));
        if (daily != null && daily.getRegistryName().equals(original.getRegistryName())) {
            daily = null; //Empty out the daily quests as they have been cleared out
            //Update the world about the daily having changed
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(p -> AdventureDataLoader.get(p.world).syncDailies(p));
        }

        setupOrRefresh(0); //Refresh everything as we have added a new script
    }

    public void fire(String method, EntityPlayer player) {
        HashMultimap.create(triggers).get(method).forEach(script -> script.fire(method, player, this));
    }

    public void markCompleted(int day, Quest quest) {
        if (completed.containsKey(quest.getRegistryName())) MapHelper.increment(completed, quest.getRegistryName());
        else completed.put(quest.getRegistryName(), 1);
        completedDay.put(quest.getRegistryName(), day);
        active.remove(quest.getRegistryName());
        setupOrRefresh(day);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("Times", NBTHelper.writeObjIntMap(completed));
        compound.setTag("Last", NBTHelper.writeObjIntMap(completedDay));
        tag.setTag("Completed", compound);
        tag.setTag("Active", QuestHelper.writeQuestMap(active));
        if (daily != null) {
            tag.setString("Daily", daily.getRegistryName().toString());
            NBTTagCompound data = new NBTTagCompound();
            daily.getInterpreter().callFunction("saveData", data);
            tag.setTag("DailyData", data);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagCompound compound = nbt.getCompoundTag("Completed");
        NBTHelper.readObjIntMap(compound.getTagList("Times", 10), completed);
        NBTHelper.readObjIntMap(compound.getTagList("Last", 10), completedDay);
        QuestHelper.readQuestMap(nbt.getTagList("Active", 10), active);
        if (nbt.hasKey("Daily")) daily = Quest.REGISTRY.get(new ResourceLocation(nbt.getString("Daily")));
        else onNewDay(null);
    }
}

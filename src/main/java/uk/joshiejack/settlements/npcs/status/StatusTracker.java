package uk.joshiejack.settlements.npcs.status;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.settlements.network.status.PacketNPCStatusUpdate;
import uk.joshiejack.settlements.network.status.PacketSyncNPCStatuses;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;

public class StatusTracker implements INBTSerializable<NBTTagCompound> {
    private final Map<ResourceLocation, Object2IntMap<String>> status = Maps.newHashMap();
    private final Map<ResourceLocation, Object2IntMap<String>> status_timers = Maps.newHashMap();
    private WeakReference<EntityPlayer> player;
    private final UUID uuid;

    public StatusTracker(UUID uuid) {
        this.uuid = uuid;
    }

    private Object2IntMap<String> getDataForNPC(Map<ResourceLocation, Object2IntMap<String>> map, ResourceLocation npc) {
        if (!map.containsKey(npc)) {
            map.put(npc, new Object2IntOpenHashMap<>());
        }

        return map.get(npc);
    }

    @Nullable
    private EntityPlayer getPlayer(World world) {
        if (player == null || player.get() == null) {
            player = new WeakReference<>(PlayerHelper.getPlayerFromUUID(world, uuid));
        }

        return player.get();
    }

    public void newDay() {
        status.keySet().forEach(npc -> {
            Object2IntMap<String> status = this.status.get(npc);
            if (status != null) {
                status.keySet().forEach(s -> {
                    if (Status.resets(s)) {
                        int value = status.getInt(s);
                        Object2IntMap<String> status_timer = getDataForNPC(this.status_timers, npc);
                        if (value > 0) MapHelper.adjustOrPut(status, s, 1, 1);;
                        int timer = status_timer.getInt(s);
                        if (timer >= Status.getReset(s)) {
                            status_timer.put(s, 0); // Reset the timer
                            status.put(s, 0); //Reset the value
                        }
                    }
                });
            }
        });

        PenguinNetwork.sendToEveryone(new PacketSyncNPCStatuses(status));
    }

    public boolean has(ResourceLocation npc, String status) {
        return this.status.get(npc).containsKey(status);
    }

    public int total(String status) {
        int count = 0;
        for (ResourceLocation k: this.status.keySet()) {
            count += this.status.get(k).getInt(status);
        }

        return count;
    }

    public int get(ResourceLocation npc, String status) {
        return this.status.containsKey(npc) ? this.getDataForNPC(this.status, npc).getInt(status) : 0;
    }

    public void set(World world, ResourceLocation npc, String status, int value) {
        if (value == 0){
            this.getDataForNPC(this.status, npc).remove(status); //Don't bother storing if the value is 0
        } else this.getDataForNPC(this.status, npc).put(status, value);
        updateClient(world, npc, status); //Sync the value to the client
    }

    public void adjust(World world, ResourceLocation npc, String status, int value) {
        value = MapHelper.adjustOrPut(this.getDataForNPC(this.status, npc), status, value, value);
        if (value == 0) this.getDataForNPC(this.status, npc).remove(status);
        updateClient(world, npc, status); //Sync the value to the client
    }

    private static NBTTagList writeNPCtoObjectIntMap(Map<ResourceLocation, Object2IntMap<String>> save) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<ResourceLocation, Object2IntMap<String>> e: save.entrySet()) {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("NPC", e.getKey().toString());
            NBTTagList map = new NBTTagList();
            for (String status: e.getValue().keySet()) {
                NBTTagCompound statusTag = new NBTTagCompound();
                statusTag.setString("Status", status);
                statusTag.setInteger("Value", e.getValue().getInt(status));
                map.appendTag(statusTag);
            }

            data.setTag("Map", map);
            list.appendTag(data);
        }

        return list;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Statuses", writeNPCtoObjectIntMap(status));
        tag.setTag("StatusTimers", writeNPCtoObjectIntMap(status_timers));
        return tag;
    }

    private static void readNPCToObjectIntMap(NBTTagList list, Map<ResourceLocation, Object2IntMap<String>> load) {
        load.clear();
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            ResourceLocation npc = new ResourceLocation(data.getString("NPC"));
            load.put(npc, new Object2IntOpenHashMap<>()); //Add for this npc as they have data...
            NBTTagList map = data.getTagList("Map", 10);
            for (int j = 0; j < map.tagCount(); j++) {
                NBTTagCompound statusTag = map.getCompoundTagAt(j);
                String status =statusTag.getString("Status");
                int value = statusTag.getInteger("Value");
                load.get(npc).put(status, value);
            }
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        readNPCToObjectIntMap(nbt.getTagList("Statuses", 10), status);
        readNPCToObjectIntMap(nbt.getTagList("StatusTimers", 10), status_timers);
    }

    private void updateClient(World world, ResourceLocation npc, String status) {
        EntityPlayer player = getPlayer(world);
        if (player != null) {
            PenguinNetwork.sendToClient(new PacketNPCStatusUpdate(npc, status, this.status.get(npc).get(status)), player);
        }
    }

    public void sync(World world) {
        EntityPlayer player = getPlayer(world);
        if (player != null) {
            PenguinNetwork.sendToClient(new PacketSyncNPCStatuses(status), player);
        }
    }
}

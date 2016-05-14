package joshie.harvest.player.relationships;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncGifted;
import joshie.harvest.core.network.PacketSyncMarriage;
import joshie.harvest.core.network.PacketSyncRelationship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.Map;

public class RelationshipDataServer extends RelationshipData {
    private HashSet<IRelatable> talked = new HashSet<>();
    private HashSet<IRelatable> temp;

    public void newDay() {
        talked = new HashSet<>();
        temp = gifted; //Clone it
        gifted = new HashSet<>();
    }

    @Override
    public void talkTo(EntityPlayer player, IRelatable relatable) {
        if (!talked.contains(relatable)) {
            affectRelationship(player, relatable, 100);
            talked.add(relatable);
        }
    }

    @Override
    public boolean gift(EntityPlayer player, IRelatable relatable, int amount) {
        if (!gifted.contains(relatable)) {
            if (amount == 0) return true;
            syncGifted((EntityPlayerMP) player, relatable, true);
            affectRelationship(player, relatable, amount);
            gifted.add(relatable);
            return true;
        }

        return false;
    }

    @Override
    public void affectRelationship(EntityPlayer player, IRelatable relatable, int amount) {
        int newValue = Math.max(0, Math.min(NPC.marriageRequirement, getRelationship(relatable) + amount));
        relationships.put(relatable, newValue);
        HFTrackers.markDirty();
        syncRelationship((EntityPlayerMP) player, relatable, newValue, true);
    }

    public void sync(EntityPlayerMP player) {
        for (IRelatable relatable : marriedTo) {
            syncMarriage(player, relatable, true);
        }

        for (IRelatable relatable : relationships.keySet()) {
            syncRelationship(player, relatable, relationships.get(relatable), false);
        }

        //
        //IF we didn't clone yesterdays, then sync the current, otherwise, destroy yesterdays after syncing
        if (temp == null) {
            for (IRelatable relatable : gifted) {
                syncGifted(player, relatable, true);
            }
        } else {
            for (IRelatable relatable: temp) {
                syncGifted(player, relatable, false);
            }

            temp = null;
        }
    }

    public void syncMarriage(EntityPlayerMP player, IRelatable relatable, boolean divorce) {
        PacketHandler.sendToClient(new PacketSyncMarriage(relatable, divorce), player);
    }

    public void syncRelationship(EntityPlayerMP player, IRelatable relatable, int value, boolean particles) {
        PacketHandler.sendToClient(new PacketSyncRelationship(relatable, value, particles), player);
    }

    public void syncGifted(EntityPlayerMP player, IRelatable relatable, boolean value) {
        PacketHandler.sendToClient(new PacketSyncGifted(relatable, value), player);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Reading Relations
        NBTTagList relationList = nbt.getTagList("Relationships", 10);
        for (int i = 0; i < relationList.tagCount(); i++) {
            NBTTagCompound tag = relationList.getCompoundTagAt(i);
            IRelatableDataHandler data = HFApi.relations.getDataHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            if (relatable != null) {
                int value = tag.getInteger("Value");
                relationships.put(relatable, value);
            }
        }

        //Reading Talked
        NBTTagList talkedList = nbt.getTagList("TalkedTo", 10);
        for (int i = 0; i < talkedList.tagCount(); i++) {
            NBTTagCompound tag = talkedList.getCompoundTagAt(i);
            IRelatableDataHandler data = HFApi.relations.getDataHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            if (relatable != null) talked.add(relatable);
        }

        //Reading Gifted
        NBTTagList giftedList = nbt.getTagList("Gifted", 10);
        for (int i = 0; i < giftedList.tagCount(); i++) {
            NBTTagCompound tag = giftedList.getCompoundTagAt(i);
            IRelatableDataHandler data = HFApi.relations.getDataHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            if (relatable != null) gifted.add(relatable);
        }

        //Reading Married
        NBTTagList marriedList = nbt.getTagList("MarriedTo", 10);
        for (int i = 0; i < marriedList.tagCount(); i++) {
            NBTTagCompound tag = marriedList.getCompoundTagAt(i);
            IRelatableDataHandler data = HFApi.relations.getDataHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            if (relatable != null) marriedTo.add(relatable);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        //Saving Relationships
        NBTTagList relationList = new NBTTagList();
        for (Map.Entry<IRelatable, Integer> entry : relationships.entrySet()) {
            if (entry == null || entry.getKey() == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = entry.getKey().getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(entry.getKey(), tag);
            tag.setInteger("Value", entry.getValue());
            relationList.appendTag(tag);
        }

        nbt.setTag("Relationships", relationList);

        //Saving Talked List
        NBTTagList talkedList = new NBTTagList();
        for (IRelatable r : talked) {
            if (r == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = r.getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(r, tag);
            talkedList.appendTag(tag);
        }

        nbt.setTag("TalkedTo", talkedList);

        //Saving Gifted List
        NBTTagList giftedList = new NBTTagList();
        for (IRelatable r : gifted) {
            if (r == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = r.getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(r, tag);
            giftedList.appendTag(tag);
        }

        nbt.setTag("Gifted", giftedList);

        //Saving Marriages
        NBTTagList marriedList = new NBTTagList();
        for (IRelatable r : marriedTo) {
            if (r == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = r.getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(r, tag);
            marriedList.appendTag(tag);
        }

        nbt.setTag("MarriedTo", marriedList);
    }
}
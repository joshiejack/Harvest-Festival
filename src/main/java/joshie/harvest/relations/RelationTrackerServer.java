package joshie.harvest.relations;

import java.util.HashSet;
import java.util.Map;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncMarriage;
import joshie.harvest.core.network.PacketSyncRelationship;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class RelationTrackerServer extends RelationshipTracker {
    private HashSet<IRelatable> talked = new HashSet();
    private HashSet<IRelatable> gifted = new HashSet();
    public PlayerTrackerServer master;

    public RelationTrackerServer(PlayerTrackerServer master) {
        this.master = master;
    }

    public void newDay() {
        talked = new HashSet();
        gifted = new HashSet();
    }

    @Override
    public void talkTo(IRelatable relatable) {
        if (!talked.contains(relatable)) {
            affectRelationship(relatable, 100);
            talked.add(relatable);
        }
    }

    @Override
    public void gift(IRelatable relatable, int amount) {
        if (!gifted.contains(relatable)) {
            affectRelationship(relatable, amount);
            gifted.add(relatable);
        }
    }

    @Override
    public void affectRelationship(IRelatable relatable, int amount) {
        short newValue = (short) (getRelationship(relatable) + amount);
        relationships.put(relatable, newValue);
        markDirty();
        syncRelationship(relatable, newValue);
    }

    private void syncMarriage(IRelatable relatable) {
        PacketHandler.sendToClient(new PacketSyncMarriage(relatable), master.getAndCreatePlayer());
    }

    private void syncRelationship(IRelatable relatable, short value) {
        PacketHandler.sendToClient(new PacketSyncRelationship(relatable, value, false), master.getAndCreatePlayer());
    }

    @Override
    public void sync() {
        for (IRelatable relatable : marriedTo) {
            syncMarriage(relatable);
        }

        for (IRelatable relatable : relationships.keySet()) {
            syncRelationship(relatable, relationships.get(relatable));
        }
    }

    @Override
    public void markDirty() {
        DataHelper.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Reading Relations
        NBTTagList relationList = nbt.getTagList("Relationships", 10);
        for (int i = 0; i < relationList.tagCount(); i++) {
            NBTTagCompound tag = relationList.getCompoundTagAt(i);
            IRelatableDataHandler data = RelationshipHelper.getHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            short value = tag.getShort("Value");
            relationships.put(relatable, value);
        }

        //Reading Talked
        NBTTagList talkedList = nbt.getTagList("TalkedTo", 10);
        for (int i = 0; i < talkedList.tagCount(); i++) {
            NBTTagCompound tag = talkedList.getCompoundTagAt(i);
            IRelatableDataHandler data = RelationshipHelper.getHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            talked.add(relatable);
        }

        //Reading Gifted
        NBTTagList giftedList = nbt.getTagList("Gifted", 10);
        for (int i = 0; i < giftedList.tagCount(); i++) {
            NBTTagCompound tag = giftedList.getCompoundTagAt(i);
            IRelatableDataHandler data = RelationshipHelper.getHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            gifted.add(relatable);
        }

        //Reading Married
        NBTTagList marriedList = nbt.getTagList("MarriedTo", 10);
        for (int i = 0; i < marriedList.tagCount(); i++) {
            NBTTagCompound tag = marriedList.getCompoundTagAt(i);
            IRelatableDataHandler data = RelationshipHelper.getHandler(tag.getString("Handler"));
            IRelatable relatable = data.readFromNBT(tag);
            marriedTo.add(relatable);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        //Saving Relationships
        NBTTagList relationList = new NBTTagList();
        for (Map.Entry<IRelatable, Short> entry : relationships.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = entry.getKey().getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(entry.getKey(), tag);
            tag.setShort("Value", entry.getValue());
            relationList.appendTag(tag);
        }

        nbt.setTag("Relationships", relationList);

        //Saving Talked List
        NBTTagList talkedList = new NBTTagList();
        for (IRelatable r : talked) {
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
            NBTTagCompound tag = new NBTTagCompound();
            IRelatableDataHandler data = r.getDataHandler();
            tag.setString("Handler", data.name());
            data.writeToNBT(r, tag);
            marriedList.appendTag(tag);
        }

        nbt.setTag("MarriedTo", marriedList);
    }
}

package joshie.harvestmoon.player;

import java.util.HashSet;
import java.util.UUID;

import joshie.harvestmoon.core.helpers.generic.EntityHelper;
import joshie.harvestmoon.core.util.IData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class FriendTracker implements IData {
    private PlayerDataServer master;
    private HashSet<UUID> friends = new HashSet();

    public FriendTracker(PlayerDataServer master) {
        this.master = master;
    }

    protected void addFriend(UUID uuid) {
        friends.add(uuid);
    }

    protected void removeFriend(UUID uuid) {
        friends.add(uuid);
    }

    protected HashSet<EntityPlayer> getFriends() {
        HashSet<EntityPlayer> players = new HashSet();
        EntityPlayer owner = master.getAndCreatePlayer();
        if (owner != null) players.add(owner);
        for (UUID uuid : friends) {
            EntityPlayer player = EntityHelper.getPlayerFromUUID(uuid);
            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList talked = nbt.getTagList("Friends", 10);
        for (int i = 0; i < talked.tagCount(); i++) {
            NBTTagCompound tag = talked.getCompoundTagAt(i);
            friends.add(new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList friends = new NBTTagList();
        for (UUID uuid : this.friends) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("UUIDMost", uuid.getMostSignificantBits());
            tag.setLong("UUIDLeast", uuid.getLeastSignificantBits());
            friends.appendTag(tag);
        }

        nbt.setTag("Friends", friends);
    }
}

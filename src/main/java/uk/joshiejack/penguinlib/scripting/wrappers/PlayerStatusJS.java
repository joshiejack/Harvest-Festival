package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSyncPlayerStatuses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStatusJS extends AbstractJS<PlayerJS> {
    public PlayerStatusJS(PlayerJS player) {
        super(player);
    }

    public int get(String status) {
        EntityPlayer player = penguinScriptingObject.penguinScriptingObject;
        return player.getEntityData().getCompoundTag("PenguinStatuses").getInteger(status);
    }

    public void set(String status, int value) {
        EntityPlayer player = penguinScriptingObject.penguinScriptingObject;
        NBTTagCompound tag = player.getEntityData().getCompoundTag("PenguinStatuses");
        player.getEntityData().setTag("PenguinStatuses", tag);
        if (value == 0) tag.removeTag(status);
        else tag.setInteger(status, value);
        if (!player.world.isRemote) {
            PenguinNetwork.sendToClient(new PacketSyncPlayerStatuses(tag), player);
        }
    }

    public void adjust(String status, int value) {
        set(status, get(status) + value);
    }

    public void adjustWithRange(String status, int value, int min, int max) {
        set(status, Math.min(max, Math.max(min, get(status) + value)));
    }
}

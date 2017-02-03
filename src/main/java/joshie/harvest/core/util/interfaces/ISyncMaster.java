package joshie.harvest.core.util.interfaces;

import joshie.harvest.api.quests.TargetType;
import joshie.harvest.quests.packet.PacketSharedSync;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface ISyncMaster {
    /** Return the target type **/
    TargetType getTargetType();

    /** Sync to everyone of if applicable only the player passed in **/
    void sync(@Nullable EntityPlayer player, PacketSharedSync packet);
}

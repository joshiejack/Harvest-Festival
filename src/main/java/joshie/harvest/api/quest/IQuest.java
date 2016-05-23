package joshie.harvest.api.quest;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

public interface IQuest {
    /** Sets the unique name of this quest **/
    IQuest setUniqueName(String name);
    
    /** Returns the unique name of this quest **/
    String getUniqueName();

    /** Sets the current stage of the quest **/
    IQuest setStage(int stage);

    /** Current stage **/
    int getStage();

    /** Return true if this quest can be started **/
    boolean canStart(EntityPlayer player, HashSet<IQuest> current, HashSet<IQuest> finished);

    /** Return true if this npc handles the script **/
    boolean handlesScript(INPC npc);

    /** Get the quest script **/
    String getScript(EntityPlayer player, EntityNPC npc);

    /** Called serverside to claim the reward **/
    void claim(EntityPlayer entityPlayer);

    /** Read quest data from nbt **/
    void readFromNBT(NBTTagCompound tag);

    /** Writes quest data to nbt **/
    void writeToNBT(NBTTagCompound tag);
    
    /** Write quest data to buffer **/
    void toBytes(ByteBuf buf);

    /** Read quest data from the buffer **/
    void fromBytes(ByteBuf buf);

    /** Called when a player closes chat while this quest is active **/
    void onClosedChat(EntityPlayer player, EntityNPC npc);

    /** Called when a quest option is confirmed **/
    void confirm(EntityPlayer player, EntityNPC npc);

    /** Called when a question option is cancelled **/
    void cancel(EntityPlayer player, EntityNPC npc);

    /** Called when the quest is active and on interacting with an entity **/
    void onEntityInteract(EntityPlayer entityPlayer, Entity target);

    /** Called when the quest is active, and the player clicks a block **/
    void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face);

    /** Called when the stage changes **/
    void onStageChanged(EntityPlayer player, int previous, int stage);
}
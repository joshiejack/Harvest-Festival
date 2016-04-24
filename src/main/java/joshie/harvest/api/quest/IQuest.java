package joshie.harvest.api.quest;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.HashSet;

public interface IQuest {
    /** Sets the unique name of this quest **/
    public IQuest setUniqueName(String name);
    
    /** Returns the unique name of this quest **/
    public String getUniqueName();

    /** Sets the current stage of the quest **/
    public IQuest setStage(int stage);

    /** Current stage **/
    public int getStage();

    /** Return true if this quest can be started **/
    public boolean canStart(EntityPlayer player, HashSet<IQuest> current, HashSet<IQuest> finished);

    /** Return true if this npc handles the script **/
    public boolean handlesScript(INPC npc);

    /** Get the quest script **/
    public String getScript(EntityPlayer player, EntityNPC npc);

    /** Called serverside to claim the reward **/
    public void claim(EntityPlayer entityPlayer);

    /** Read quest data from nbt **/
    public void readFromNBT(NBTTagCompound tag);

    /** Writes quest data to nbt **/
    public void writeToNBT(NBTTagCompound tag);
    
    /** Write quest data to buffer **/
    public void toBytes(ByteBuf buf);

    /** Read quest data from the buffer **/
    public void fromBytes(ByteBuf buf);

    /** Called when a player closes chat while this quest is active **/
    public void onClosedChat(EntityPlayer player, EntityNPC npc);

    /** Called when a quest option is confirmed **/
    public void confirm(EntityPlayer player, EntityNPC npc);

    /** Called when a question option is cancelled **/
    public void cancel(EntityPlayer player, EntityNPC npc);

    /** Called when the quest is active and on interacting with an entity **/
    public void onEntityInteract(EntityPlayer entityPlayer, Entity target);

    /** Called when the quest is active, and the player clicks a block **/
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face);

    /** Called when the stage changes **/
    public void onStageChanged(EntityPlayer player, int previous, int stage);
}

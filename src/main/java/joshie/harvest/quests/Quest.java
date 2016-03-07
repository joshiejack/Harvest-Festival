package joshie.harvest.quests;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToServer;

import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quest.IQuest;
import joshie.harvest.core.network.quests.PacketQuestSetStage;
import joshie.harvest.core.util.Translate;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Quest implements IQuest {
    protected String name;
    protected int quest_stage;
    protected Set<IQuest> required;

    public Quest() {}
    
    @Override
    public IQuest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
    }
    
    @Override
    public int getStage() {
        return quest_stage;
    }
    
    @Override
    public IQuest setUniqueName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    /** ENSURE YOU ONLY EVER CALL THIS ON ONE SIDE **/
    protected void increaseStage(EntityPlayer player) {
        int previous = this.quest_stage;
        this.quest_stage++;
        if (!player.worldObj.isRemote) {
            //Send Packet to increase stage to client
            sendToClient(new PacketQuestSetStage(this, false, this.quest_stage), (EntityPlayerMP) player);
        } else {
            //Send Packet to increase stage to server
            sendToServer(new PacketQuestSetStage(this, true, this.quest_stage));
        }
        
        onStageChanged(player, previous, quest_stage);
    }

    protected boolean isRepeatable() {
        return false;
    }
    
    public int getOptions() {
        return 0;
    }
    
    public void onSelected(EntityPlayer player, int option) {}

    //This is only called client side
    @Override
    public boolean canStart(EntityPlayer player, HashSet<IQuest> active, HashSet<IQuest> finished) {
        if(finished.contains(this) && !isRepeatable()) {
            return false;
        }
        
        if (required != null) {
            if (!finished.containsAll(required)) {
                return false;
            }
        }
        
        //Loops through all the active quests, if any of the quests contain npcs that are used by this quest, we can not start it
        for (IQuest a : active) {
            for(INPC npc: getNPCs()) {
                if(a.handlesScript(npc)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    //Return a list of all the npcs involved in this quest
    protected abstract INPC[] getNPCs();

    //Translates a key, to up to 10 lines for the language
    protected String getLocalized(String quest) {
        return Translate.translate("quest." + name + "." + quest.replace("_", ""));
    }

    /** Exposed to quest_stage */
    //Return true if the script will determine, thisNPC's script at the current stage   
    @SideOnly(Side.CLIENT)
    @Override
    public boolean handlesScript(INPC npc) {
        for(INPC n: getNPCs()) {
            if(n.equals(npc)) {
                return true;
            }
        }
        
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, INPC npc) {
        return null;
    }

    /** Exposed to quest_stage, is only called client side, must sync any changes */
    //Return the script
    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityNPC npc) {
        String script = getScript(player, npc.getNPC());
        return script == null? null: getLocalized(script);
    }

    //Called Serverside, to claim the reward
    @Override
    public void claim(EntityPlayer player) {
        return;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        quest_stage = nbt.getShort("Completed");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("Completed", (short) quest_stage);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(quest_stage);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        quest_stage = buf.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof IQuest)) return false;

        return name.equals(((IQuest) o).getUniqueName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**** EVENTS ****/
    //Called When a player interacts with an animal
    @Override
    public void onEntityInteract(EntityPlayer player, Entity target) {}

    //Called serverside when you close the chat with an npc
    public void onClosedChat(EntityPlayer player, EntityNPC npc) {}

    @Override
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {}

    public void select(EntityPlayer player, EntityNPC npc, int option) {}
    public void confirm(EntityPlayer player, EntityNPC npc) {}
    public void cancel(EntityPlayer player, EntityNPC npc) {}
    
    @Override
    public void onStageChanged(EntityPlayer player, int previous, int stage) {}
}

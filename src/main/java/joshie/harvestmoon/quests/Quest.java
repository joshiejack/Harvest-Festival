package joshie.harvestmoon.quests;

import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import static joshie.harvestmoon.network.PacketHandler.sendToServer;
import io.netty.buffer.ByteBuf;

import java.util.HashSet;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.network.quests.PacketQuestSetStage;
import joshie.harvestmoon.util.Translate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Quest {
    protected String name;
    protected int quest_stage;

    public Quest() {}

    public Quest setName(String name) {
        this.name = name;
        return this;
    }

    public Quest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getStage() {
        return quest_stage;
    }

    /** ENSURE YOU ONLY EVER CALL THIS ON ONE SIDE **/
    public void increaseStage(EntityPlayer player) {
        this.quest_stage++;
        if (!player.worldObj.isRemote) {
            //Send Packet to increase stage to client
            sendToClient(new PacketQuestSetStage(this, false, this.quest_stage), (EntityPlayerMP) player);
        } else {
            //Send Packet to increase stage to server
            sendToServer(new PacketQuestSetStage(this, true, this.quest_stage));
        }
    }

    public boolean isRepeatable() {
        return false;
    }

    //This is only called client side
    public boolean canStart(EntityPlayer player, HashSet<Quest> active, HashSet<Quest> finished) {
        if(finished.contains(this) && !isRepeatable()) {
            return false;
        }
        
        //Loops through all the active quests, if any of the quests contain npcs that are used by this quest, we can not start it
        for (Quest a : active) {
            for(NPC npc: getNPCs()) {
                if(a.handlesScript(npc)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    //Return a list of all the npcs involved in this quest
    public NPC[] getNPCs() {
        return null;
    }

    //Translates a key, to up to 10 lines for the language
    protected String getLocalized(String quest) {
        return Translate.translate("quest." + name + "." + quest);
    }

    /** Exposed to quest_stage */
    //Return true if the script will determine, thisNPC's script at the current stage   
    @SideOnly(Side.CLIENT)
    public boolean handlesScript(NPC npc) {
        for(NPC n: getNPCs()) {
            if(n.equals(npc)) {
                return true;
            }
        }
        
        return false;
    }

    /** Exposed to quest_stage, is only called client side, must sync any changes */
    //Return the script
    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, EntityNPC npc) {
        return null;
    }

    //Called Serverside, to claim the reward
    public void claim(EntityPlayerMP player) {
        return;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        quest_stage = nbt.getShort("Completed");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("Completed", (short) quest_stage);
    }

    public void toBytes(ByteBuf buf) {
        buf.writeShort(quest_stage);
    }

    public void fromBytes(ByteBuf buf) {
        quest_stage = buf.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;

        return name.equals(((Quest) o).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**** EVENTS ****/
    //Called When a player interacts with an animal
    public void onEntityInteract(EntityPlayer player, Entity target) {}

    //Called serverside when you close the chat with an npc
    public void onClosedChat(EntityPlayer player, EntityNPC npc) {}

    public void onRightClickBlock(EntityPlayer player, World world, int x, int y, int z, int side) {}
}

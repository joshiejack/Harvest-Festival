package uk.joshiejack.settlements.entity.ai.action.chat;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionChat;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.network.npc.PacketSay;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.util.Strings;

@PenguinLoader("say")
public class ActionSay extends ActionMental implements ActionChat {
    public String text;
    public String[] formatting;
    private boolean displayed;
    private boolean read;

    @Override
    public ActionSay withData(Object... params) {
        if (params.length == 0) {
             this.text = "text";
             this.formatting = new String[0];
        } else {
            this.text = (String) params[0];
            this.formatting = new String[params.length - 1];
            for (int i = 1; i < params.length; i++) {
                this.formatting[i - 1] = String.valueOf(params[i]);
            }
        }

        if (this.text == null) this.text = Strings.EMPTY; //No null
        return this;
    }

    @Override
    public void onGuiClosed(EntityPlayer player, EntityNPC npc, Object... parameters) {
        read = true;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (!displayed && player != null) {
            displayed = true; //Marked it as displayed
            npc.addTalking(player); //Add the talking
            PenguinNetwork.sendToClient(new PacketSay(player, npc, this), player);
        }

        return player == null || npc.IsNotTalkingTo(player) || read ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("RegistryName", registryName.toString());
        tag.setBoolean("IsQuest", isQuest);
        tag.setString("Text", text);
        tag.setByte("FormattingLength", (byte) formatting.length);
        for (int i = 0; i < formatting.length; i++) {
            tag.setString("Formatting" + i, formatting[i]);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        registryName = new ResourceLocation(tag.getString("RegistryName"));
        isQuest = tag.getBoolean("IsQuest");
        text = tag.getString("Text");
        formatting = new String[tag.getByte("FormattingLength")];
        for (int i = 0; i < formatting.length; i++) {
            formatting[i] = tag.getString("Formatting" + i);
        }
    }
}

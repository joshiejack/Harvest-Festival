package joshie.harvest.quests.town.festivals.starry;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarryNightData extends Selection {
    private static final String prefix = "harvestfestival.quest.festival.starry.night";
    private boolean completed;
    private boolean chat;
    private NPC invited;

    public StarryNightData() {
        super("What do you want to do?", "@Invite to Starry Night", "@Chat");
    }

    public Selection getSelection() {
        return invited == null ? this : null;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalized(String quest, Object... format) {
        if (format.length == 0) return I18n.translateToLocal(prefix + "." + quest.replace("_", ""));
        else return I18n.translateToLocalFormatted(prefix + "." + quest.replace("_", ""), format);
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(NPC npc) {
        if (chat) return null;
        else if (invited == null) return "Please invite me";
        else return invited == npc ? "I look forward to seeing you tonight.": null;
    }

    public boolean isChatting() {
        if (chat) {
            chat = false;
            return true;
        } else return false;
    }

    public boolean isFinished() {
        return completed;
    }

    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, Quest quest, int option) {
        //Option1 = Invite to Starry Night
        if (option == 1) invited = entity.getNPC();
        else chat = true;
        quest.syncData(player); //Resync to client
        //Option2 = Chat
        return Result.ALLOW;
    }

    /////////////////////////////// Saving and Loading /////////////////////////////////
    public static StarryNightData fromNBT(NBTTagCompound tag) {
        StarryNightData data =  new StarryNightData();
        data.chat = tag.getBoolean("Chatting");
        data.completed = tag.getBoolean("Completed");
        if (tag.hasKey("NPC")) data.invited = NPC.REGISTRY.get(new ResourceLocation(tag.getString("NPC")));
        return data;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Completed", completed);
        tag.setBoolean("Chatting", chat);
        if (invited != null) tag.setString("NPC", invited.getResource().toString());
        return tag;
    }
}

package joshie.harvest.quests.town.festivals.starry;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.quests.town.festivals.QuestStarryNight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Set;

public class StarryNightData extends Selection<QuestStarryNight> {
    private static final String prefix = "harvestfestival.quest.festival.starry.night";
    private static final String[] lines2 = new String[] { "Are you ready to eat?", "@Let's begin!", "@Not Yet!"};
    private boolean completed;
    private boolean chat;
    private NPC invited;

    public StarryNightData() {
        super("What do you want to do?", "@Invite to Starry Night", "@Chat");
    }

    @Override
    public String[] getText(@Nonnull EntityPlayer player, QuestStarryNight quest) {
        return invited == null ? lines : lines2;
    }

    public Selection getSelection(long time) {
        return !chat && (invited == null || time >= 18000L || time < 6000L) ? this : null;
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
        else if (invited == null) return TextHelper.getSpeech(npc, "festival.starry.night.invite");
        else return invited == npc ? TextHelper.getSpeech(npc, "festival.starry.night.tonight") : null;
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

    public boolean isInvited(NPCEntity npc) {
        return invited != null && (invited == npc.getNPC() || invited.getFamily().contains(npc.getNPC()));
    }

    private void startSequence(EntityPlayer player, NPCEntity entity) {
        Set<NPC> family = entity.getNPC().getFamily(); //Family
        //Walk around and have a speech
    }

    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestStarryNight quest, int option) {
        if (invited == null) {
            //Option1 = Invite to Starry Night
            if (option == 1) invited = entity.getNPC();
            else chat = true;
        } else {
            if (option == 1) {
                chat = true;
                completed = true;
                startSequence(player, entity);
            } else return Result.DENY;
        }

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

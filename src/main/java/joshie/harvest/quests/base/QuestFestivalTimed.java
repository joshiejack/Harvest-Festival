package joshie.harvest.quests.base;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.calendar.CalendarHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class QuestFestivalTimed extends QuestFestival {
    protected abstract boolean isCorrectTime(long time);

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (!isCorrectTime(CalendarHelper.getTime(player.worldObj))) return null; //Don't process
        return getLocalizedScript(player, npc);
    }

    @Nullable
    protected abstract String getLocalizedScript(EntityPlayer player, NPC npc);

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (!isCorrectTime(CalendarHelper.getTime(player.worldObj))) return; //Don't process
        onChatClosed(player, npc);
    }

    public abstract void onChatClosed(EntityPlayer player, NPC npc);
}

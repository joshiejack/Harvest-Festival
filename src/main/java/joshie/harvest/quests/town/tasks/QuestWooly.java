package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestWeekly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;
import static joshie.harvest.core.helpers.InventoryHelper.SearchType.WOOL;

@HFQuest("friendship.wooly")
public class QuestWooly extends QuestWeekly {
    private Result success = Result.DEFAULT;
    private CalendarDate requested;

    public QuestWooly() {
        setNPCs(HFNPCs.FLOWER_GIRL);
        setTownQuest();
    }

    @Override
    public boolean canStartDailyQuest(World world, BlockPos pos) {
        return HFApi.calendar.getWeather(world).isSnow() && super.canStartDailyQuest(world, pos);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        return "Bring Jade some wool";
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (requested == null) requested = HFApi.calendar.getDate(player.worldObj).copy();
        int days = CalendarHelper.getDays(requested, HFApi.calendar.getDate(player.worldObj));
        if (days < getDaysBetween()) {
            if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, WOOL) != null) {
                return "Woah you are amazing, thanks for getting me this wool, now I can wrap up nice and warm for winter.";
            } else if (player.worldObj.rand.nextFloat() < 0.05F) return "I'd love some wool so I can make some new clothes for winter";
        } else {
            return "Are you sure you actually care about me? You've taken way too long, I've got my own clothes now!";
        }

        return null;

    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        if (requested == null) requested = HFApi.calendar.getDate(player.worldObj).copy();
        int days = CalendarHelper.getDays(requested, HFApi.calendar.getDate(player.worldObj));
        if (days < getDaysBetween()) {
            if (InventoryHelper.takeItemsIfHeld(player,SPECIAL, WOOL) != null) {
                success = Result.ALLOW;
                complete(player);
            }
        } else {
            success = Result.DENY;
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (success == Result.ALLOW) HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.FLOWER_GIRL.getUUID(), 500);
         else if (success == Result.DENY) HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.FLOWER_GIRL.getUUID(), -100);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("RequestedDate")) {
            requested = CalendarDate.fromNBT(nbt.getCompoundTag("RequestedDate"));
            success = Result.valueOf(nbt.getString("Success"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (requested != null) {
            nbt.setTag("RequestedDate", requested.toNBT());
            nbt.setString("Success", success.name());
        }

        return nbt;
    }
}

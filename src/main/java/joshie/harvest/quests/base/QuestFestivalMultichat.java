package joshie.harvest.quests.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class QuestFestivalMultichat extends QuestFestival {
    private final Multimap<UUID, NPC> received = HashMultimap.create();

    protected boolean isCorrectTime(long time) {
        return true;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        if (!isCorrectTime(CalendarHelper.getTime(player.worldObj)) || received.get(EntityHelper.getPlayerUUID(player)).contains(entity.getNPC())) return null; //Don't process
        return getLocalizedScript(player, entity.getNPC());
    }

    @Nullable
    protected abstract String getLocalizedScript(EntityPlayer player, NPC npc);

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (isCorrectTime(CalendarHelper.getTime(player.worldObj))) {
            UUID uuid = EntityHelper.getPlayerUUID(player);
            if (received.get(uuid).contains(entity.getNPC())) return;
            received.get(uuid).add(entity.getNPC()); //Mark this npc as talked to
            syncData(player); //Update the data about this npc
            onChatClosed(player, entity.getNPC());
        }
    }

    public abstract void onChatClosed(EntityPlayer player, NPC npc);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        received.clear(); //Reset the internal data
        NBTTagList listIds = nbt.getTagList("Received", 10);
        for (int i = 0; i < listIds.tagCount(); i++) {
            NBTTagCompound tag = listIds.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("ID"));
            NBTTagList npcIds = tag.getTagList("NPCs", 8);
            for (int j = 0; j < npcIds.tagCount(); j++) {
                received.get(uuid).add(NPC.REGISTRY.getValue(new ResourceLocation(npcIds.getStringTagAt(j))));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList listIDs = new NBTTagList();
        for (UUID uuid: received.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ID", uuid.toString());
            NBTTagList npcIDs = new NBTTagList();
            for (NPC npc: received.get(uuid)) {
                npcIDs.appendTag(new NBTTagString(npc.getRegistryName().toString()));
            }

            tag.setTag("NPCs", npcIDs);
            listIDs.appendTag(tag);
        }

        nbt.setTag("Received", listIDs);

        return super.writeToNBT(nbt);
    }
}

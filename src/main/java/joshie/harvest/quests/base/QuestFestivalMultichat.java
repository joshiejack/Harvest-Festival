package joshie.harvest.quests.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityLiving;
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

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (received.get(EntityHelper.getPlayerUUID(player)).contains(npc)) return null; //Don't process
        return getLocalizedScript(player, npc);
    }

    @Nullable
    protected abstract String getLocalizedScript(EntityPlayer player, NPC npc);

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        UUID uuid = EntityHelper.getPlayerUUID(player);
        if (received.get(uuid).contains(npc)) return;
        received.get(uuid).add(npc); //Mark this npc as talked to
        syncData(player); //Update the data about this npc
        onChatClosed(player, npc);
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

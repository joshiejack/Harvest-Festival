package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.quests.town.festivals.Place;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ContestEntry<Q extends QuestContest> {
    protected final UUID player;
    protected final NPC npc;
    protected final int stall;

    public ContestEntry(UUID player, int stall) {
        this.player = player;
        this.npc = null;
        this.stall = stall;
    }

    public ContestEntry(NPC npc, int stall) {
        this.player = null;
        this.npc = npc;
        this.stall = stall;
    }

    public NPC getNPC() {
        return npc;
    }

    @Nullable
    public EntityPlayer getPlayer(World world) {
        return player != null ? EntityHelper.getPlayerFromUUID(world, player) : null;
    }

    public abstract String getName(World world);

    public String getOwnerName(World world) {
        EntityPlayer player = getPlayer(world);
        return player != null ? player.getName() : npc != null ? npc.getLocalizedName() : "Anonymous";
    }

    public abstract boolean isInvalid(World world);
    public abstract int getScore(Q q, World world);
    public abstract void reward(World world, Place place, NPC[] npcs, ItemStack reward);

    public int getStall() {
        return stall;
    }

    @Nullable
    public UUID getPlayerUUID() {
        return player;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Stall", stall);
        if (player != null) tag.setString("Player", player.toString());
        else if (npc != null) tag.setString("NPC", npc.getResource().toString());
        return tag;
    }

    public abstract String getTextFromScore(String unlocalised, int score);
}

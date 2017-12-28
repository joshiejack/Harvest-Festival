package joshie.harvest.core.tile;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.base.tile.TileStand;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.NPCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class TileFestivalPot extends TileStand {
    private final Map<UUID, ItemStack> data = new HashMap<>();

    @Override
    public boolean canEmpty() { return false; }

    @Override
    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        if (player == null || data.containsKey(EntityHelper.getPlayerUUID(player))) return false;
        ItemStack insert = stack.splitStack(1);
        data.put(EntityHelper.getPlayerUUID(player), insert);
        super.stack = data.get(EntityHelper.getPlayerUUID(player));
        saveAndRefresh();
        return true;
    }

    public ItemStack getContents(EntityPlayer player) {
        return data.get(EntityHelper.getPlayerUUID(player));
    }

    @Override
    public ItemStack removeContents() {
        return null; //Can't remove what you put in
    }

    private int getScoreFromStack(ItemStack stack) {
        GiftCategory primary = NPCHelper.INSTANCE.getGifts().getRegistry().getValueOf(stack);
        if (primary != null) {
            switch (primary) {
                case BUILDING:
                case MONSTER:
                case JUNK:
                    return 1;
                case GEM:
                case MINERAL:
                    return 2;
                case KNOWLEDGE:
                case MAGIC:
                case WOOL:
                    return 3;
                case FLOWER:
                case PLANT:
                    return 4;
                case FISH:
                    return 5;
                case MEAT:
                case EGG:
                case MILK:
                    return 6;
                case COOKING:
                    return 7;
                case VEGETABLE:
                    return 8;
                case FRUIT:
                    return 9;
                case HERB:
                    return 10;
            }
        }

        return 0;
    }

    public int getScore() {
        double score = 0;
        for (ItemStack stack: data.values()) {
            score += getScoreFromStack(stack);
        }

        return (int) (score / data.size());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList list = nbt.getTagList("Data", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            ItemStack stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Data"));
            data.put(uuid, stack);
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList list = new NBTTagList();
        for (Entry<UUID, ItemStack> entry: data.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", entry.getKey().toString());
            tag.setTag("Data", entry.getValue().writeToNBT(new NBTTagCompound()));
            list.appendTag(tag);
        }

        nbt.setTag("Data", list);
        return nbt;
    }
}

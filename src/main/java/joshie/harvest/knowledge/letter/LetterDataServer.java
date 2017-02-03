package joshie.harvest.knowledge.letter;

import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.core.Letter;
import joshie.harvest.core.util.interfaces.ISyncMaster;
import joshie.harvest.knowledge.packet.PacketAddLetter;
import joshie.harvest.knowledge.packet.PacketRemoveLetter;
import joshie.harvest.knowledge.packet.PacketSyncLetters;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class LetterDataServer extends LetterData {
    protected TObjectIntMap<Letter> letters = new TObjectIntHashMap<>(); //Letters Unread with the number of days left
    private ISyncMaster master;

    public LetterDataServer(ISyncMaster master) {
        this.master = master;
    }

    @Override
    public Set<Letter> getLetters() {
        return letters.keySet();
    }

    @Override
    public boolean hasUnreadLetters() {
        return letters.size() > 0;
    }

    public void add(Letter letter) {
        letters.put(letter, 0);
    }

    public void remove(Letter letter) {
        letters.remove(letter);
    }

    public void addLetter(Letter letter) {
        letters.put(letter, 0);
        master.sync(null, new PacketAddLetter(letter));
    }

    public void removeLetter(Letter letter) {
        letters.remove(letter);
        master.sync(null, new PacketRemoveLetter(letter));
    }

    public void sync(EntityPlayerMP player) {
        master.sync(player, new PacketSyncLetters(letters.keySet()));
    }

    public void newDay(CalendarDate today) {
        //Increase the number of days passed by one
        for (Letter resource: letters.keySet()) {
            letters.increment(resource);
        }

        //Now to check if we need to remove, then remove if so
        boolean changed = false;
        TObjectIntIterator<Letter> it = letters.iterator();
        while (it.hasNext()) {
            it.advance(); //Advance the iterator
            Letter letter = it.key();
            if (letter.isExpired(today, it.value())) {
                changed = true;
                it.remove();
            }
        }

        if (changed) {
            master.sync(null, new PacketSyncLetters(letters.keySet()));
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("LetterData", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ResourceLocation resource = new ResourceLocation(tag.getString("Letter"));
            int days = tag.getInteger("Days");
            letters.put(Letter.REGISTRY.get(resource), days);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (Letter letter: letters.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Letter", letter.getResource().toString());
            tag.setInteger("Days", letters.get(letter));
            list.appendTag(tag);
        }

        nbt.setTag("LetterData", list);
        return nbt;
    }
}

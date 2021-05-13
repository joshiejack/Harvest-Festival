package uk.joshiejack.harvestcore.world.storage;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.harvestcore.network.mail.PacketReceiveLetter;
import uk.joshiejack.harvestcore.network.mail.PacketSendLetter;
import uk.joshiejack.harvestcore.network.mail.PacketSyncLetters;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinGroup;
import uk.joshiejack.penguinlib.util.helpers.generic.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class Mailroom implements INBTSerializable<NBTTagCompound> {
    private final NonNullList<Letter> unread_letters = NonNullList.create();
    private final Object2IntMap<ResourceLocation> timer = new Object2IntOpenHashMap<>();
    private final Object2IntMap<ResourceLocation> deliver_timer = new Object2IntOpenHashMap<>();
    private WeakReference<EntityPlayer> player;
    private final UUID uuid;

    public Mailroom(UUID uuid) {
        this.uuid = uuid;
    }

    @Nullable
    public EntityPlayer getPlayer(World world) {
        if (player == null || player.get() == null) {
            player = new WeakReference<>(PlayerHelper.getPlayerFromUUID(world, uuid));
        }

        return player.get();
    }

    public void copyLetters(World world, Mailroom theirmailroom) {
        unread_letters.removeIf(letter -> letter.getGroup() == PenguinGroup.TEAM);
        theirmailroom.unread_letters.stream()
                .filter((letter -> letter.getGroup() == PenguinGroup.TEAM))
                .forEach(unread_letters::add);
        SavedData.save(world);
        sendPacket(world, new PacketSyncLetters(unread_letters));
    }

    public void synchronize(World world) {
        sendPacket(world, new PacketSyncLetters(unread_letters));
    }

    public void onNewDay(World world) {
        for (ResourceLocation resource: Sets.newHashSet(timer.keySet())) {
            Letter letter = Letter.REGISTRY.get(resource);
            if (MapHelper.adjustOrPut(timer, letter.getRegistryName(), 1, 0) >= letter.getExpiry()) {
                if (letter.isRepeatable()) timer.remove(letter.getRegistryName());
                if (letter.expires()) unread_letters.remove(letter);
            }
        }

        //YES MATE
        for (ResourceLocation letter: Sets.newHashSet(deliver_timer.keySet())) {
            MapHelper.adjustValue(deliver_timer, letter, -1);
            if (deliver_timer.getInt(letter) <= 0) {
                deliver_timer.remove(letter);
                deliverLetter(world, Letter.REGISTRY.get(letter)); //Yes this is how we roll byatch!!
            }
        }

        synchronize(world);
    }

    public void removeLetter(World world, Letter letter) {
        unread_letters.remove(letter);
        if (letter.isRepeatable()) {
            timer.remove(letter.getRegistryName());
        }

        sendPacket(world, new PacketReceiveLetter(letter));
        SavedData.save(world);
    }

    public void addLetter(World world, Letter letter) {
        if (letter.getDelay() > 0) {
            deliver_timer.put(letter.getRegistryName(), letter.getDelay());
        } else deliverLetter(world, letter);
    }

    private void deliverLetter(World world, Letter letter) {
        //Don't receive the letter if we already have it
        if (!timer.containsKey(letter.getRegistryName())) {
            unread_letters.add(letter);
            timer.put(letter.getRegistryName(), 0);
            sendPacket(world, new PacketSendLetter(letter));
            SavedData.save(world);
        }
    }

    private void sendPacket(World world, PenguinPacket packet) {
        EntityPlayer player = getPlayer(world);
        if (player != null) {
            PenguinNetwork.sendToClient(packet, player);
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Letters", NBTHelper.writePenguinRegistry(unread_letters));
        tag.setTag("LetterTimer", NBTHelper.writeObjIntMap(timer));
        tag.setTag("DeliverTimer", NBTHelper.writeObjIntMap(deliver_timer));
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTHelper.readPenguinRegistry(nbt.getTagList("Letters", 8), Letter.REGISTRY, unread_letters);
        NBTHelper.readObjIntMap(nbt.getTagList("LetterTimer", 10), timer);
        NBTHelper.readObjIntMap(nbt.getTagList("DeliverTimer", 10), timer);
    }
}

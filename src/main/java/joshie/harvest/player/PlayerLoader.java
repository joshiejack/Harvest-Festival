package joshie.harvest.player;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

@HFEvents
public class PlayerLoader {
    private static File PLAYER_DIR;
    private static File getFolder(File playerDir) {
        File dir = new File(playerDir, "HF");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static PlayerTrackerServer getDataFromUUID(@Nullable EntityPlayerMP player, UUID uuid) {
        PlayerTrackerServer data = new PlayerTrackerServer(player, uuid);
        File file = new File(getFolder(PLAYER_DIR), uuid + ".dat");
        if (!file.exists() && player != null) file = new File(getFolder(PLAYER_DIR), EntityHelper.getLastKnownUUID(player) + ".dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                NBTTagCompound tag = CompressedStreamTools.readCompressed(fileinputstream);
                fileinputstream.close();
                data.readFromNBT(tag);
            } catch (Exception e) { e.printStackTrace(); }
        }

        HFTrackers.setPlayerData(uuid, data);
        return data;
    }

    @SubscribeEvent
    public void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        PLAYER_DIR = event.getPlayerDirectory(); //Refresh the directory
        getDataFromUUID((EntityPlayerMP) event.getEntityPlayer(), EntityHelper.getPlayerUUID(event.getEntityPlayer()));
    }

    //Setup the Player
    @SubscribeEvent
    public void onPlayerSave(PlayerEvent.SaveToFile event) {
        try {
            PlayerTrackerServer data = HFTrackers.getPlayerTrackerFromPlayer(event.getEntityPlayer());
            File file = new File(getFolder(event.getPlayerDirectory()), EntityHelper.getPlayerUUID(event.getEntityPlayer()) + ".dat");
            NBTTagCompound tag = data.writeToNBT(new NBTTagCompound());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            CompressedStreamTools.writeCompressed(tag, fileoutputstream);
            fileoutputstream.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}

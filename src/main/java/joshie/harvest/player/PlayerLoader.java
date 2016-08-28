package joshie.harvest.player;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@HFEvents
public class PlayerLoader {
    private File getFolder(File playerDir) {
        File dir = new File(playerDir, "HF");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    @SubscribeEvent
    public void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        PlayerTrackerServer data = new PlayerTrackerServer((EntityPlayerMP)event.getEntityPlayer());
        File file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getPlayerUUID(event.getEntityPlayer()) + ".dat");
        if (!file.exists()) file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getLastKnownUUID(event.getEntityPlayer()) + ".dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                NBTTagCompound tag = CompressedStreamTools.readCompressed(fileinputstream);
                fileinputstream.close();
                data.readFromNBT(tag);
            } catch (Exception e) { e.printStackTrace(); }
        }

        HFTrackers.setPlayerData(event.getEntityPlayer(), data);
    }

    //Setup the Player
    @SubscribeEvent
    public void onPlayerSave(PlayerEvent.SaveToFile event) {
        try {
            PlayerTrackerServer data = HFTrackers.getPlayerTracker(event.getEntityPlayer());
            File file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getPlayerUUID(event.getEntityPlayer()) + ".dat");
            NBTTagCompound tag = data.writeToNBT(new NBTTagCompound());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            CompressedStreamTools.writeCompressed(tag, fileoutputstream);
            fileoutputstream.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}

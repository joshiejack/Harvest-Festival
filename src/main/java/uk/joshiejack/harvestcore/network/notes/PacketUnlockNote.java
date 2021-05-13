package uk.joshiejack.harvestcore.network.notes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketUnlockNote extends PacketSendPenguin<Note> {
    public PacketUnlockNote() { super(Note.REGISTRY); }
    public PacketUnlockNote(Note note) {
        super(Note.REGISTRY, note);
    }

    @Override
    public void handlePacket(EntityPlayer player, Note note) {
        note.unlock(player);
    }
}
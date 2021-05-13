package uk.joshiejack.harvestcore.tile;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.client.mail.Inbox;
import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.tile.TileRotatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

@PenguinLoader("mailbox")
public class TileMailbox extends TileRotatable implements IInteractable {
    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        if (player.world.isRemote) {
            if (Inbox.getLetters().size() > 0) {
                player.openGui(HarvestCore.instance, 0, player.world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }

        return false;
    }
}

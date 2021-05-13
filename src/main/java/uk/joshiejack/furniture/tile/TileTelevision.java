package uk.joshiejack.furniture.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.network.PacketSetTVProgram;
import uk.joshiejack.furniture.television.TVProgram;
import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.tile.TileRotatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("television")
public class TileTelevision extends TileRotatable implements IInteractable {
    private TVProgram program = TVProgram.OFF;

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        player.openGui(Furniture.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Nonnull
    public TVProgram getProgram() {
        return program;
    }

    public void setProgram(TVProgram program) {
        this.program = program;
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSetTVProgram(pos, program));
        }
    }
}

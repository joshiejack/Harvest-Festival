package uk.joshiejack.harvestcore.network.blueprint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketUnlockBlueprint extends PacketSendPenguin<Blueprint> {
    public PacketUnlockBlueprint() { super(Blueprint.REGISTRY); }
    public PacketUnlockBlueprint(Blueprint blueprint) {
        super(Blueprint.REGISTRY, blueprint);
    }

    @Override
    public void handlePacket(EntityPlayer player, Blueprint blueprint) {
        blueprint.unlock(player);
    }
}

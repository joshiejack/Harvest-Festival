package uk.joshiejack.energy.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.energy.EnergyFoodStats;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncStats extends PenguinPacket {
    private int maxFoodDisplay;
    private double maxHealth;

    public PacketSyncStats() {}
    public PacketSyncStats(EnergyFoodStats stats) {
        maxFoodDisplay = stats.maxFoodDisplay;
        maxHealth = stats.maxHearts;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(maxFoodDisplay);
        to.writeDouble(maxHealth);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        maxFoodDisplay = from.readByte();
        maxHealth = from.readDouble();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        EnergyFoodStats stats = player.getFoodStats() instanceof EnergyFoodStats ? (EnergyFoodStats) player.getFoodStats() : new EnergyFoodStats(player);
        if (player.getFoodStats() != stats)
            ReflectionHelper.setPrivateValue(EntityPlayer.class, player, stats, "foodStats", "field_71100_bB");
        else {
            stats.maxFoodDisplay = maxFoodDisplay;
            stats.maxHearts = maxHealth;
            player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(stats.maxHearts);
        }
    }
}

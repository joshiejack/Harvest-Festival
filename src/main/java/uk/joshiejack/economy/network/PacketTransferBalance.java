package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.gold.Vault;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

@PenguinLoader(side = Side.SERVER)
public class PacketTransferBalance extends PenguinPacket {
    private Wallet.Type from;
    private long gold;

    public PacketTransferBalance() {}
    public PacketTransferBalance(Wallet.Type type, long gold) {
        this.from = type;
        this.gold = gold;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeBoolean(from == Wallet.Type.SHARED);
        to.writeLong(gold);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        this.from = from.readBoolean() ? Wallet.Type.SHARED : Wallet.Type.PERSONAL;
        gold = from.readLong();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        UUID playerUUID = PlayerHelper.getUUIDForPlayer(player);
        Vault teamVault = Bank.get(player.world).getVaultForTeam(team.getID());
        Vault playerVault = Bank.get(player.world).getVaultForTeam(playerUUID);
        if (from == Wallet.Type.PERSONAL) {
            long actual = Math.min(gold, playerVault.getBalance());
            playerVault.personal().setBalance(player.world, playerVault.getBalance() - actual);
            teamVault.shared().setBalance(player.world, teamVault.getBalance() + actual);
        } else {
            long actual = Math.min(gold, teamVault.getBalance());
            teamVault.shared().setBalance(player.world, teamVault.getBalance() - actual);
            playerVault.personal().setBalance(player.world, playerVault.getBalance() + actual);
        }
    }
}

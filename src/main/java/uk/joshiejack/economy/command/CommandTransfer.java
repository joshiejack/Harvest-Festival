package uk.joshiejack.economy.command;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.gold.Vault;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.UUID;

import static uk.joshiejack.economy.Economy.MODID;

@PenguinLoader
public class CommandTransfer extends EconomyCommand {
    @Nonnull
    @Override
    public String getName() {
        return "transfer";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return MODID + ".commands.transfer.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayer && args.length == 1) {
            EntityPlayer player = (EntityPlayer) sender;
            PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
            UUID playerUUID = PlayerHelper.getUUIDForPlayer(player);
            long transfer = Long.parseLong(args[0]); //We take the maximum we can
            if (transfer > 0) {
                boolean shared = player.getEntityData().hasKey(Economy.MODID + "Settings") &&
                        player.getEntityData().getCompoundTag(Economy.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
                Vault teamVault = Bank.get(sender.getEntityWorld()).getVaultForTeam(team.getID());
                Vault playerVault = Bank.get(sender.getEntityWorld()).getVaultForTeam(playerUUID);
                if (shared) {
                    long actual = Math.min(transfer, playerVault.getBalance());
                    playerVault.personal().setBalance(player.world, playerVault.getBalance() - actual);
                    teamVault.shared().setBalance(player.world, teamVault.getBalance() + actual);
                } else {
                    long actual = Math.min(transfer, teamVault.getBalance());
                    teamVault.shared().setBalance(player.world, teamVault.getBalance() - actual);
                    playerVault.personal().setBalance(player.world, playerVault.getBalance() + actual);
                }
            }
        }
    }
}

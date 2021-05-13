package uk.joshiejack.economy.command;

import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.gold.Vault;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

import static uk.joshiejack.economy.Economy.MODID;

@PenguinLoader
public class CommandGold extends EconomyCommand {
    @Nonnull
    @Override
    public String getName() {
        return "gold";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return MODID + ".commands.gold.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length > 1) {
            EntityPlayer target = args.length == 3 ? sender.getEntityWorld().getPlayerEntityByName(args[2]) : sender instanceof EntityPlayer ? (EntityPlayer) sender : null;
            if (target != null) {
                Vault vault = Bank.get(sender.getEntityWorld()).getVaultForPlayer(target);
                long gold = Long.parseLong(args[1]);
                if (args[0].equals("add")) {
                    vault.setBalance(target.world, vault.getBalance() + gold);
                } else if (args[0].equals("set")) {
                    vault.setBalance(target.world, gold);
                }
            }
        }
    }
}

package uk.joshiejack.economy.command;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.network.PacketSetWalletUsed;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

import static uk.joshiejack.economy.Economy.MODID;

@PenguinLoader
public class CommandShare extends EconomyCommand {
    @Nonnull
    @Override
    public String getName() {
        return "share";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return MODID + ".commands.share.usage";
    }

    private static void setShared(EntityPlayer player, boolean shared) {
        if (!player.getEntityData().hasKey(Economy.MODID + "Settings")) {
            player.getEntityData().setTag(Economy.MODID + "Settings", new NBTTagCompound());
        }

        player.getEntityData().getCompoundTag(Economy.MODID + "Settings").setBoolean("SharedGold", shared);
        PenguinNetwork.sendToClient(new PacketSetWalletUsed(shared), player);
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 1) {
            EntityPlayer player = getCommandSenderAsPlayer(sender);
            if (args[0].equals("enable")) {
                setShared(player, true);
            } else  if (args[0].equals("disable")) {
                setShared(player, false);
            }

            //Resync the new values
            Bank.get(sender.getEntityWorld()).syncToPlayer(player);
            Market.get(sender.getEntityWorld()).getShippingForPlayer(player).syncToPlayer(player);
        }
    }
}

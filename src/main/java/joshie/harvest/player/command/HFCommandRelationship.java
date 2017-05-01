package joshie.harvest.player.command;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandRelationship extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "relationship";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf relationship [player] <npc|all> <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 2 || parameters.length == 3) {
            EntityPlayerMP player = parameters.length == 2 ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
            RelationshipDataServer relationships = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships();
            String npc = parameters[parameters.length - 2];
            int value = Integer.parseInt(parameters[parameters.length - 1]);
            switch (npc) {
                case "all":
                    NPC.REGISTRY.values().stream().forEach(npcz -> relationships.affectRelationship(npcz, value));
                    break;
                case "clear":
                    NPC.REGISTRY.values().stream().forEachOrdered(npcz -> relationships.affectRelationship(npcz, -relationships.getRelationship(npcz)));
                    break;
                default:
                    if (!npc.contains(":")) npc = "harvestfestival:" + npc;
                    NPC theNPC = NPC.REGISTRY.get(new ResourceLocation(npc));
                    if (theNPC == null) return;
                    else {
                        relationships.affectRelationship(theNPC, value);
                        return;
                    }
            }
        } else throw new WrongUsageException(getCommandUsage(sender));
    }
}

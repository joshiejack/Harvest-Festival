package joshie.harvest.player.command;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.relationships.RelationshipDataServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandRelationship extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "relationship";
    }

    @Override
    public String getUsage() {
        return "/hf relationship [player] <npc|all> <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && (parameters.length == 2 || parameters.length == 3)) {
            try {
                EntityPlayerMP player = parameters.length == 2 ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
                RelationshipDataServer relationships = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getRelationships();
                String npc = parameters[parameters.length - 2];
                int value = Integer.parseInt(parameters[parameters.length - 1]);
                switch (npc) {
                    case "all":
                        NPC.REGISTRY.values().stream().forEach(npcz -> relationships.affectRelationship(npcz, value));
                        return true;
                    case "clear":
                        NPC.REGISTRY.values().stream().forEachOrdered(npcz -> relationships.affectRelationship(npcz, -relationships.getRelationship(npcz)));
                        return true;
                    default:
                        if (!npc.contains(":")) npc = "harvestfestival:" + npc;
                        relationships.affectRelationship(NPC.REGISTRY.get(new ResourceLocation(npc)), value);
                        return true;
                }
            } catch (NumberFormatException | PlayerNotFoundException ignored) {}
        }

        return false;
    }
}

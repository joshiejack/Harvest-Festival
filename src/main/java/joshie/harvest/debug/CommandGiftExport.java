package joshie.harvest.debug;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.npcs.NPCHelper;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandGiftExport extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "export-gifts";
    }

    public static StringBuilder getGifts(ItemStack stack) {
        if (!NPCHelper.INSTANCE.getGifts().isBlacklisted(stack)) {
            HashMultimap<Quality, NPC> qualities = HashMultimap.create();
            for (NPC npc : NPC.REGISTRY) {
                if (npc == NPC.NULL_NPC) continue;
                Quality quality = npc.getGiftValue(stack);
                qualities.get(quality).add(npc);
            }

            StringBuilder builder = new StringBuilder();
            builder.append("==Gifts==\n");
            builder.append("{{Gifts");
            for (Quality quality: Quality.values()) {
                if (qualities.get(quality).size() > 0) {
                    builder.append("|");
                    builder.append(quality.name().toLowerCase());
                    builder.append("=");

                    List<NPC> list = new ArrayList<>(qualities.get(quality));
                    Collections.sort(list, (o1, o2) -> {
                        return o1.getLocalizedName().compareTo(o2.getLocalizedName());
                    });

                    boolean first = false;
                    for (NPC npc: list) {
                        if (!first) first = true;
                        else builder.append(",");
                        builder.append(npc.getLocalizedName());
                    }

                    builder.append("\n");
                }
            }

            builder.append("}}");
            return builder;
        }

        return Debug.none();
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (sender instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer)sender).getHeldItemMainhand();
            if (stack != null) {
                Debug.save(getGifts(stack));
            }
        }

        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }
}

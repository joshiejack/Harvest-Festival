package joshie.harvest.debug;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftHandler;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.holders.HolderRegistry;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.gift.Gifts;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;
import java.util.Map.Entry;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFCommand
public class CommandGiftExportNPC extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "export-npcs";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (parameters.length == 1) {
            NPC npc = NPCRegistry.REGISTRY.getValue(new ResourceLocation(MODID, parameters[0]));
            if (npc != null) {
                IGiftHandler gifts = ReflectionHelper.getPrivateValue(NPC.class, npc, "gifts");
                if (gifts instanceof Gifts) {
                    final HolderRegistry<Quality> stackRegistry = ReflectionHelper.getPrivateValue(Gifts.class, (Gifts)gifts, "stackRegistry");
                    final EnumMap<GiftCategory, Quality> categoryRegistry = ReflectionHelper.getPrivateValue(Gifts.class, (Gifts)gifts, "categoryRegistry");
                    HashMultimap<Quality, GiftCategory> qualityToCategories = HashMultimap.create();
                    for (Entry<GiftCategory, Quality> entry: categoryRegistry.entrySet()) {
                        qualityToCategories.get(entry.getValue()).add(entry.getKey());
                    }

                       //Build everything

                    Set<ItemStack> handled = new HashSet<>();
                    StringBuilder builder2 = new StringBuilder();
                    List<ItemStack> exceptions = new ArrayList<>();
                    for (Quality quality: Quality.values()) {
                        exceptions.addAll(stackRegistry.getStacksFor(quality));
                    }

                    for (Quality quality: Quality.values()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("===" + WordUtils.capitalize(quality.name().toLowerCase()) + "===");
                        builder.append("\n{{quote|");
                        builder.append(TextHelper.getSpeech(npc, "gift." + quality.name().toLowerCase(Locale.ENGLISH)));
                        builder.append("}} \n" +
                                "{| class=\"wikitable sortable\" id=\"roundedborder\" style=\"width:65%; min-width:500px;\"");
                        builder.append("\n!Image\n" +
                                "!Gift Type\n" +
                                "!Description\n" +
                                "!Source");
                         builder.append("\n" +
                                "\n");

                        for (GiftCategory category: qualityToCategories.get(quality)) {
                            StringBuilder eString = new StringBuilder();
                            boolean initial = false;
                            for (ItemStack stack: exceptions) {
                                GiftCategory[] r = NPCRegistry.INSTANCE.getGifts().getRegistry().getValueOf(stack);
                                if (r != null) {
                                    for (GiftCategory sCategory : r) {
                                        if (sCategory == category) {
                                            if (!initial) {
                                                eString.append(stack.getDisplayName());
                                                initial = true;
                                            } else {
                                                eString.append(", ");
                                                eString.append(stack.getDisplayName());
                                            }
                                        }
                                    }
                                }
                            }


                            List<ItemStack> stacks = NPCRegistry.INSTANCE.getGifts().getRegistry().getStacksFor(new GiftCategory[] { category });
                            builder.append("|-\n" +
                                    "| [[File:" + WordUtils.capitalize(category.name().toLowerCase()) + " Category.png|48px|center]]\n" +
                                    "|''[[Gifting#" + WordUtils.capitalize(category.name().toLowerCase()) + "|" + WordUtils.capitalize(category.name().toLowerCase()) + "|48px|center]]''\n" +
                                    "|''");

                            if (initial) {
                                builder.append("All Except for ");
                                builder.append(eString);
                            } else builder.append("All");

                            builder.append("''\n" +
                                    "|\n");
                        }

                        for (ItemStack stack: stackRegistry.getStacksFor(quality)) {
                            builder.append("|-\n" +
                                    "|[[File:" + stack.getDisplayName() + ".png|center]]\n" +
                                    "|[[" + stack.getDisplayName() + "]]\n" +
                                    "|\n" +
                                    "|\n");
                            handled.add(stack);
                        }

                        builder.append("|}\n\n");
                        builder2.append(builder);
                    }

                    Debug.save(builder2);
                }

            }
        }

        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }
}

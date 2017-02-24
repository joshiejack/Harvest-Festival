package joshie.harvest.debug;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.StateHandlerBasic;
import joshie.harvest.api.crops.StateHandlerBlock;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.gift.IGiftHandler.Quality;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFDebugCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandCropExport extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "export-crop";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (sender instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer)sender).getHeldItemMainhand();
            if (stack != null) {
                Crop crop = HFApi.crops.getCropFromStack(stack);
                if (crop != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("{{Infobox crop\n" +
                            "|image       = ");
                    builder.append(crop.getLocalizedName(true));
                    builder.append(".png\n" +
                            "|seed        = [[");
                    builder.append(crop.getSeedsName());
                    builder.append("]]\n" +
                            "|growth      = ");
                    builder.append(crop.getStages());
                    builder.append(" days\n" +
                            "|season      = ");
                    boolean first = false;
                    for (Season season: crop.getSeasons()) {
                        if (!first) first = true;
                        else builder.append(", ");
                        builder.append("[[");
                        builder.append(WordUtils.capitalize(season.name().toLowerCase()));
                        builder.append("]]");
                    }

                    builder.append("\n" +
                            "|price       = ");
                    builder.append(crop.getSellValue());
                    builder.append("G\n" +
                            "|edibility   = ");
                    ItemFood food = stack.getItem() instanceof ItemFood ? (ItemFood)stack.getItem() : null;
                    if (food == null) builder.append("N/A");
                    else {
                        builder.append("{{name|Hunger|");
                        builder.append(food.getHealAmount(stack));
                        builder.append("}}{{name|Saturation|");
                        builder.append(food.getSaturationModifier(stack));
                        builder.append("}}");
                    }

                    builder.append("\n" +
                            "}}\n" +
                            "The '''");
                    builder.append(crop.getLocalizedName(true));
                    builder.append("''' is a ");
                    if (crop.getSeasons().length == 1) {
                        builder.append("[[");
                        builder.append(WordUtils.capitalize(crop.getSeasons()[0].name().toLowerCase()));
                        builder.append("]]");
                    } else {
                        builder.append("multi seasonal");
                    }

                    builder.append(" [[crop]] in [[Harvest Festival]]. They take ");
                    builder.append(crop.getStages());
                    builder.append(" days to grow and will ");
                    if (crop.getRegrowStage() == 0) {
                        builder.append("only produce one crop.");
                    } else {
                        builder.append("regrow new ");
                        builder.append(crop.getFoodType().name().toLowerCase());
                        builder.append(" every ");
                        builder.append(crop.getStages() - crop.getRegrowStage());
                        builder.append(" days thereafter.");
                    }

                    //Generate the stages
                    if (crop.getStateHandler() instanceof StateHandlerBasic || crop.getStateHandler() instanceof StateHandlerBlock) {
                        builder.append("\n\n==Stages==" +
                                "\n{| class=wikitable style=\"text-align:center;\" id=\"roundedborder\"");

                        int[] values = crop.getStateHandler() instanceof StateHandlerBasic ? ReflectionHelper.getPrivateValue(StateHandlerBasic.class, ((StateHandlerBasic)crop.getStateHandler()), "values") :
                                ReflectionHelper.getPrivateValue(StateHandlerBlock.class, ((StateHandlerBlock)crop.getStateHandler()), "values");
                        for (int i = 1; i < values.length; i++) {
                            builder.append("\n!Stage ");
                            builder.append(i);
                        }

                        builder.append("\n!Harvest");
                        if (crop.getRegrowStage() != 0) builder.append("\n!Regrow");
                        builder.append("\n|-");
                        for (int i = 1; i <= values.length; i++) {
                            builder.append("\n|[[File:");
                            builder.append(crop.getLocalizedName(true));
                            builder.append(" Stage ");
                            builder.append(i);
                            builder.append(".png|center|link=]]");
                        }

                        if (crop.getRegrowStage() != 0) {
                            builder.append("\n|[[File:");
                            builder.append(crop.getLocalizedName(true));
                            builder.append(" Stage ");
                            builder.append(values.length - 1);
                            builder.append(".png|center|link=]]");
                        }

                        builder.append("\n|-");

                        for (int i = 0; i < values.length - 1; i++) {
                            builder.append("\n|");
                            int previous = i - 1 <= 0 ? values[0] : values[i - 1];
                            if (i - 1 < 0) builder.append(values[0]);
                            else builder.append(values[i] - previous);
                            builder.append(" Days");
                        }

                        builder.append("\n|Total: ");
                        builder.append(crop.getStages());
                        builder.append(" Days");
                        if (crop.getRegrowStage() != 0) {
                            builder.append("\n|Regrows every ");
                            builder.append(crop.getStages() - crop.getRegrowStage());
                            builder.append(" days");
                        }

                        builder.append("\n" +
                                "|-\n" +
                                "|}");
                    }

                    //GIFTS!
                    HashMultimap<Quality, NPC> qualities = HashMultimap.create();
                    for (NPC npc : NPC.REGISTRY) {
                        if (npc == NPC.NULL_NPC) continue;
                        Quality quality = npc.getGiftValue(stack);
                        qualities.get(quality).add(npc);
                    }

                    builder.append("\n\n==Gifts==");
                    builder.append("\n{{Gifts");
                    for (Quality quality: Quality.values()) {
                        if (qualities.get(quality).size() > 0) {
                            builder.append("|");
                            builder.append(quality.name().toLowerCase());
                            builder.append("=");

                            List<NPC> list = new ArrayList<>(qualities.get(quality));
                            Collections.sort(list, (o1, o2) -> o1.getLocalizedName().compareTo(o2.getLocalizedName()));

                            boolean first2 = false;
                            for (NPC npc: list) {
                                if (!first2) first2 = true;
                                else builder.append(",");
                                builder.append(npc.getLocalizedName());
                            }

                            builder.append("\n");
                        }
                    }

                    builder.append("}}\n");
                    builder.append(CommandExportUsageInRecipes.getExport(crop.getCropStack(1)));

                    builder.append("\n" +
                            "{{NavboxCrop}}[[Category:");
                    if (crop.getSeasons().length == 1) {
                        builder.append(WordUtils.capitalize(crop.getSeasons()[0].name().toLowerCase()));
                    } else builder.append("Multi Seasonal");
                    builder.append(" crops]]");
                    Debug.save(builder);
                }
            }
        }

        return true;
    }
}

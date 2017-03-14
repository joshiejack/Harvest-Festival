package joshie.harvest.debug.export;

import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.AbstractExport;
import joshie.harvest.debug.CommandExportUsageInRecipes;
import joshie.harvest.debug.CommandGiftExport;
import joshie.harvest.debug.Debug;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static joshie.harvest.gathering.block.BlockNature.NaturalBlock.*;

@HFDebugCommand(true)
@SuppressWarnings("all")
public class CommandNaturalExport extends AbstractExport<NaturalBlock> {
    public CommandNaturalExport() {
        super(NaturalBlock.class, HFGathering.NATURE);
        register(MATSUTAKE, "{{name|Autumn}}", "a mushroom", "It will only spawn during the [[Autumn]].");
        register(BAMBOO, "{{name|Spring}}", "a plant", "It will only spawn during the [[Spring]].");
        register(MINT, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}", "a herb", "It can be found in [[Spring]], [[Summer]] or [[Autumn]].");
        register(CHAMOMILE, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}", "a herb", "It can be found in [[Spring]], [[Summer]] or [[Autumn]].");
        register(LAVENDER, "{{name|Autumn}}{{name|Winter}}", "a herb", "It can be found in [[Autumn]] or [[Winter]].");
    }

    @Override
    protected void export(ItemStack stack, NaturalBlock eNum) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.#####");
        builder.append("{{Infobox gathering\n" +
                "|image       = " + stack.getDisplayName() + ".png\n" +
                "|season      = " + (LOCATIONS.containsKey(eNum) ? LOCATIONS.get(eNum).replace("[[", "'''[[").replace("]]", "]]'''").replace("/", "<br>") : "//TODO") + "\n" +
                "|price = {{gold|" + eNum.getSellValue() + "}}\n" +
                "}}");

        builder.append("\n").append("'''" + stack.getDisplayName() + "''' is " + IS.get(eNum) + " that can be found scattered out in the [[Wilderness]] surrounding your town. " + (DESCRIPTIONS.containsKey(eNum) ? DESCRIPTIONS.get(eNum).replace("\"", "") : "//TODO"));
        builder.append("\n\n");
        builder.append(CommandGiftExport.getGifts(stack));
        builder.append("\n");
        builder.append(CommandExportUsageInRecipes.getExport(stack));
        builder.append("\n{{NavboxGathering}}[[Category:Gathering]]");
        Debug.save(builder);
    }
}

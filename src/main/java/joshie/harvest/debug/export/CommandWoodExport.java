package joshie.harvest.debug.export;

import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.AbstractExport;
import joshie.harvest.debug.CommandGiftExport;
import joshie.harvest.debug.Debug;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockWood.Wood;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static joshie.harvest.gathering.block.BlockWood.Wood.*;

@HFDebugCommand(true)
@SuppressWarnings("all")
public class CommandWoodExport extends AbstractExport<Wood> {
    public CommandWoodExport() {
        super(Wood.class, HFGathering.WOOD);
        String low = "a branch";
        String high = "a stump";
        register(BRANCH_SMALL, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", low, "It will spawn in any season.");
        register(BRANCH_MEDIUM, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", low, "It will spawn in any season.");
        register(BRANCH_LARGE, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", low, "It will spawn in any season.");
        register(STUMP_SMALL, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", high, "It will spawn in any season.");
        register(STUMP_MEDIUM, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", high, "It will spawn in any season.");
        register(STUMP_LARGE, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", high, "It will spawn in any season.");
    }

    @Override
    protected void export(ItemStack stack, Wood eNum) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.#####");
        builder.append("{{Infobox gathering\n" +
                "|image       = " + stack.getDisplayName() + ".png\n" +
                "|season      = " + (LOCATIONS.containsKey(eNum) ? LOCATIONS.get(eNum).replace("[[", "'''[[").replace("]]", "]]'''").replace("/", "<br>") : "//TODO") + "\n" +
                "|price = {{gold|" + eNum.getSellValue() + "}}\n" +
                "}}");

        builder.append("\n").append("'''" + stack.getDisplayName() + "''' is " + IS.get(eNum) + " that can be found scattered out in the [[Wilderness]] surrounding your town. " + (DESCRIPTIONS.containsKey(eNum) ? DESCRIPTIONS.get(eNum).replace("\"", "") + " There is also a chance for them to spawn whenever your crops die replacing the dead crop." : "//TODO"));
        builder.append("\n\n");
        builder.append(CommandGiftExport.getGifts(stack));
        builder.append("\n{{NavboxGathering}}[[Category:Gathering]]");
        Debug.save(builder);
    }
}

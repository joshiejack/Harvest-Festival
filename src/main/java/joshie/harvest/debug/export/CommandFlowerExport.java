package joshie.harvest.debug.export;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.AbstractExport;
import joshie.harvest.debug.CommandGiftExport;
import joshie.harvest.debug.Debug;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static joshie.harvest.core.block.BlockFlower.FlowerType.*;

@HFDebugCommand(true)
@SuppressWarnings("all")
public class CommandFlowerExport extends AbstractExport<FlowerType> {
    public CommandFlowerExport() {
        super(FlowerType.class, HFCore.FLOWERS);
        register(WEED, "{{name|Spring}}{{name|Summer}}{{name|Autumn}}", "a plant", "It can be found in [[Spring]], [[Summer]] or [[Autumn]]. You can also find weeds in the [[Mine]].");
        register(MOONDROP, "{{name|Spring}}", "a flower", "It will only spawn during the [[Spring]].");
        register(TOY, "{{name|Spring}}", "a flower", "It will only spawn during the [[Spring]].");
        register(PINKCAT, "{{name|Summer}}", "a flower", "It will only spawn during the [[Summer]].");
        register(BLUE_MAGICGRASS, "{{name|Autumn}}", "a flower", "It will only spawn during [[Autumn]]");
        register(RED_MAGICGRASS, "{{name|Autumn}}", "a flower", "It will only spawn during [[Autumn]]");
    }

    @Override
    protected void export(ItemStack stack, FlowerType eNum) {
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
        builder.append("\n{{NavboxGathering}}[[Category:Gathering]]");
        Debug.save(builder);
    }
}

package joshie.harvest.debug.export;

import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.AbstractExport;
import joshie.harvest.debug.CommandExportUsageInRecipes;
import joshie.harvest.debug.CommandGiftExport;
import joshie.harvest.debug.Debug;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.*;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static joshie.harvest.fishing.item.ItemFish.Fish.*;
import static joshie.harvest.fishing.item.ItemFish.*;

@HFDebugCommand(true)
@SuppressWarnings("all")
public class CommandFishExport extends AbstractExport<Fish> {
    public CommandFishExport() {
        super(Fish.class, HFFishing.FISH);
        register(GOLD, "[[Pond]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in the fishing hole in any season.\"");
        register(ANCHOVY, "[[Ocean]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in the ocean in any season.\"");
        register(ANGEL, "[[River]]{{name|Spring}}", "\"in rivers in [[Spring]].\"");
        register(ANGLER, "[[Ocean]]{{name|Winter}}", "\"in the ocean in [[Winter]].\"");
        register(BASS, "[[Pond]]/[[River]]{{name|Spring}}{{name|Autumn}}", "\"in rivers or in the fishing hole in [{Spring]] or [[Autumn]]..\"");
        register(BLAASOP, "[[Ocean]]{{name|Winter}}", "\"in the ocean in [[Winter]].\"");
        register(BOWFIN, "[[Lake]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in lakes in any season.\"");
        register(BUTTERFLY, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean in [[Spring]] or [[Summer]].\"");
        register(CARP, "[[Pond]]/[[Lake]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in lakes or the fishing hole in any season.\"");
        register(CATFISH, "[[River]]{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in rivers in [[Summer]], [[Autumn]] or [[Winter]].\"");
        register(CHUB, "[[Lake]]/[[River]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in lakes or rivers in any season.\"");
        register(CLOWN, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean in [[Spring]] or [[Summer]].\"");
        register(COD, "[[Ocean]]{{name|Spring}}{{name|Autumn}}{{name|Winter}}", "\"in the ocean in [[Spring]], [[Autumn]] or [[Winter]].\"");
        register(DAMSEL, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean in [[Spring]] or [[Summer]].\"");
        register(ELECTRICRAY, "[[Ocean]]{{name|Autumn}}{{name|Winter}}", "\"in the ocean in [[Autumn]] or [[Winter]].\"");
        register(KOI, "[[Pond]]{{name|Summer}}{{name|Winter}}", "\"in the fishing hole in [[Summer]] or [[Winter]].\"");
        register(LAMPREY, "[[Ocean]]{{name|Autumn}}{{name|Winter}}[[Lake]]{{name|Spring}}", "\"in the ocean in [[Autumn]] or [[Winter]]. They can also be found in lakes in [[Spring]].\"");
        register(LUNGFISH, "[[Lake]]{{name|Summer}}", "\"in lakes in the [[Summer]].\"");
        register(MANTARAY, "[[Ocean]]{{name|Spring}}", "\"in the ocean in [[Spring]].\"");
        register(MINNOW, "[[Pond]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}[[Lake]]/[[River]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}", "\"in the fishing hole in any season. They can also be found in lakes or rivers in [[Spring]], [[Summer]] or [[Autumn]].\"");
        register(PERCH, "[[Lake]]{{name|Summer}}{{name|Autumn}}{{name|Winter}}[[River]]{{name|Spring}}{{name|Winter}}", "\"in lakes in [[Summer]], [[Autumn]] or [[Winter]]. They can also be found in rivers in the [[Spring]] or [[Winter]].\"");
        register(PICKEREL, "[[Lake]]{{name|Spring}}{{name|Summer}}{{name|Winter}}", "\"in lakes in [[Spring]], [[Summer]] or [[Winter]].\"");
        register(PIKE, "[[River]]{{name|Summer}}{{name|Winter}}", "\"in rivers in [[Summer]] or [[Winter]].\"");
        register(PIRANHA, "[[River]]{{name|Summer}}", "\"in the river in the [[Summer]].\"");
        register(PUFFER, "[[Ocean]]{{name|Summer}}", "\"in the ocean in the [[Summer]].\"");
        register(PUPFISH, "[[Lake]]{{name|Spring}}{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in lakes in any season.\"");
        register(HERRING, "[[Ocean]]{{name|Spring}}{{name|Autumn}}{{name|Winter}}", "\"in the ocean in [[Spring]], [[Autumn]] or [[Winter]].\"");
        register(SALMON, "[[Lake]]{{name|Autumn}}{{name|Winter}}[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in lakes in [[Autumn]] or [[Winter]]. They can also be found in the ocean in [[Spring]] or [[Summer]].\"");
        register(SARDINE, "[[Ocean]]{{name|Summer}}{{name|Autumn}}{{name|Winter}}", "\"in the ocean in [[Summer]], [[Autumn]] or [[Winter]].\"");
        register(SIAMESE, "[[Pond]]{{name|Spring}}{{name|Winter}}", "\"in the fishing hole in [[Spring]] or [[Winter]].\"");
        register(STARGAZER, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean in [[Spring]] or [[Summer]].\"");
        register(STINGRAY, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean [[Spring]] or [[Summer]].\"");
        register(TANG, "[[Ocean]]{{name|Spring}}{{name|Summer}}", "\"in the ocean [[Spring]] or [[Summer]].\"");
        register(TETRA, "[[River]]{{name|Spring}}{{name|Summer}}", "\"in rivers in [[Spring]] or [[Summer]].\"");
        register(TROUT, "[[Ocean]]{{name|Autumn}}[[Lake]]{{name|Spring}}[[River]]{{name|Winter}}{{name|Summer}}", "\"in the ocean in [[Autumn]]. Also found in lakes in [[Spring]] as well as in rivers in [[Summer]] and [[Winter]].\"");
        register(TUNA, "[[Ocean]]{{name|Autumn}}{{name|Winter}}", "\"in the ocean in [[Autumn]] or [[Winter]].\"");
        register(WALLEYE, "[[Pond]]{{name|Summer}}{{name|Autumn}}[[Lake]]/[[River]]{{name|Autumn}}", "\"in the fishing hole in [[Summer]] and [[Autumn]]. They can also be found in lakes or rivers in [[Autumn]].\"");

    }

    @Override
    protected boolean isExportable(ItemStack stack) {
        return stack.getItem() == HFFishing.FISH;
    }

    @Override
    public Fish getEnumFromStack(ItemStack stack) {
        return HFFishing.FISH.getEnumFromStack(stack);
    }

    @Override
    protected void export(ItemStack stack, Fish fish) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.#####");
        builder.append("{{Infobox fish\n" +
                "|image       = " + stack.getDisplayName() + ".png\n" +
                "|size        = " + format.format(fish.getLengthFromSizeOfFish(SMALL_FISH)) + "-" + format.format(fish.getLengthFromSizeOfFish(GIANT_FISH)) + "cm\n" +
                "|location      = " + (LOCATIONS.containsKey(fish) ? LOCATIONS.get(fish).replace("[[", "'''[[").replace("]]", "]]'''") : "//TODO") + "\n" +
                "|hunger      = {{hunger|" + ((ItemFood)stack.getItem()).getHealAmount(stack) + "}}\n" +
                "|price = {{gold|" + fish.getSellValue(fish.getLengthFromSizeOfFish(SMALL_FISH)) + "}} Small<br>\n" +
                "{{gold|" + fish.getSellValue(fish.getLengthFromSizeOfFish(MEDIUM_FISH)) + "}} Medium<br>\n" +
                "{{gold|" + fish.getSellValue(fish.getLengthFromSizeOfFish(LARGE_FISH)) + "}} Large<br>\n" +
                "{{gold|" + fish.getSellValue(fish.getLengthFromSizeOfFish(GIANT_FISH)) + "}} Giant\n" +
                "}}");

        builder.append("\n").append("The '''" + stack.getDisplayName() + "''' is a type of [[fish]] that can be caught " + (DESCRIPTIONS.containsKey(fish) ? DESCRIPTIONS.get(fish).replace("\"", "") : "//TODO"));
        builder.append("\n\n");
        builder.append(CommandGiftExport.getGifts(stack));
        builder.append("\n");
        builder.append(CommandExportUsageInRecipes.getExport(stack));
        builder.append("\n{{NavboxFish}}[[Category:Fish]]");
        Debug.save(builder);
    }
}

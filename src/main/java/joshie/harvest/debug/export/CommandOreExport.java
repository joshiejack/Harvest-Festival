package joshie.harvest.debug.export;

import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.debug.AbstractExport;
import joshie.harvest.debug.CommandGiftExport;
import joshie.harvest.debug.Debug;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;

import static joshie.harvest.mining.item.ItemMaterial.Material.*;

@HFDebugCommand(true)
@SuppressWarnings("all")
public class CommandOreExport extends AbstractExport<Material> {
    public CommandOreExport() {
        super(Material.class, HFMining.MATERIALS);
        register(ADAMANTITE, "[[Rock]]", "a gem that is found inside of [[Rock]] nodes. They can only be found from floor 10 and below of the mine. They can be used to buy [[Elevators]] from [[Brandon]] in the [[Mine]].");
        register(AGATE, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can only be found from floor 1-50 as well as from floor 101 and below.");
        register(ALEXANDRITE, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can be found in every floor that is divisible by 10 from floor 101 and below. Therefore the first floor they can be found on is floor 110.");
        register(AMETHYST, "[[Amethyst Node]]/[[Gem Node]]", "a gem that is found inside of [[Amethyst Nodes]] and [[Gem Nodes]]. You can find them in the [[Amethyst Nodes]] on any floor. While you will only find them in [[Gem Nodes]] from floor 1-40.");
        register(FLUORITE, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can only be found from floor 10-60 as well as from floor 101 and below of the mine.");
        register(MOON_STONE, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can only be found from floor 70 and lower in the mine.");
        register(ORICHALC, "[[Rock]]", "a gem that is found inside of [[Rock]] nodes. They can only be found from floor 10 and below of the mine.");
        register(PERIDOT, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can only be found from floor 20-70 as well as from floor 101 and below.");
        register(PINK_DIAMOND, "[[Diamond Node]]/[[Gem Node]]", "a gem that is found inside of [[Diamond Nodes]] and [[Gem Nodes]]. There is a rare 1/512 chance for them to drop from [[Diamond Nodes]]. You can also find them in [[Gem Nodes]] on every floor that is divisible by 3 from floor 101 and below. This makes the first floor they're available be floor 102.");
        register(RUBY, "[[Ruby Node]]/[[Gem Node]]", "a gem that is found inside of [[Ruby Nodes]] and [[Gem Nodes]]. You can find them in the [[Ruby Nodes]] from floor 85 and below. While you will only find them in [[Gem Nodes]] from floor 40-100.");
        register(SAND_ROSE, "[[Gem Node]]", "a gem that is found inside of [[Gem Nodes]]. They can only be found from floor 60 and below.");
        register(TOPAZ, "[[Topaz Node]]/[[Gem Node]]", "a gem that is found inside of [[Topaz Nodes]] and [[Gem Nodes]]. You can find them in the [[Topaz Nodes]] from floor 43 and below. While you will only find them in [[Gem Nodes]] from floor 30-80.");
        register(JUNK, "[[Rock]]", "an ore that is found inside of [[Rock]] nodes. They can be found on any floor. Junk Ore is used to repair your normal tools.");
        register(COPPER, "[[Copper Node]]", "is found inside of [[Copper Nodes]]. They can be found on any floor. Copper is the first tier of upgrade for your tools and is required to purchase [[Old Sprinklers]], [[Elevators]] and [[Elevator Cables]].");
        register(SILVER, "[[Silver Node]]", "is found inside of [[Silver Nodes]]. They can be found from floor 43 and below. Silver is the second tier of upgrade for your tools and is required to purchase [[Sprinklers]].");
        register(GOLD, "[[Gold Node]]", "is found inside of [[Golde Nodes]]. They can be found from floor 85 and below. Gold is the third tier of upgrade for your tools and is required to purchase [[Quest Boards]].");
        register(MYSTRIL, "[[Mystril Node]]", "is found inside of [[Mystril Nodes]]. They can be found from floor 127 and below. Mystril is the fourth tier of upgrade for your tools. It is not currently required to purchase anything.");
        register(MYTHIC, "[[Rock]]", "is found inside of [[Rock]] nodes. You can only obtain it once certain conditions are met. You will need to have obtained all of the [[Cursed Tools]] and upgraded them all to [[Blessed Tools]] before you will ever see this drop. When these conditions are met you can find mythic on floor 30, 60, 80, 85, 100, 115, 130, 140, 150, 160, 170, 175, 180, 185, 190, 195, 200, 205, 210, 213, 216, 219, 222, 225, 228, 231, 234, 237, 240, 243, 246, 249, 252 and any floor from 255. ");
    }

    @Override
    protected void export(ItemStack stack, Material eNum) {
        StringBuilder builder = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0.#####");
        builder.append("{{Infobox ore\n" +
                "|image       = " + stack.getDisplayName() + ".png\n" +
                "|source      = " + (LOCATIONS.containsKey(eNum) ? LOCATIONS.get(eNum).replace("[[", "'''[[").replace("]]", "]]'''").replace("/", "<br>") : "//TODO") + "\n" +
                "|price = {{gold|" + eNum.getSellValue() + "}}\n" +
                "}}");

        builder.append("\n").append("'''" + stack.getDisplayName() + "''' is " + (DESCRIPTIONS.containsKey(eNum) ? DESCRIPTIONS.get(eNum).replace("\"", "") : "//TODO"));
        builder.append("\n\n");
        builder.append(CommandGiftExport.getGifts(stack));
        builder.append("\n{{NavboxMining}}[[Category:Mining]]");
        Debug.save(builder);
    }
}

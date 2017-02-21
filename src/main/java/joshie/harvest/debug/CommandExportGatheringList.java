package joshie.harvest.debug;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.commands.HFDebugCommand;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import joshie.harvest.gathering.block.BlockRock.Rock;
import joshie.harvest.gathering.block.BlockWood.Wood;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportGatheringList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-gathering-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[Gathering]]\n");

        List<String> plants = new ArrayList<>();
        for (NaturalBlock nature: NaturalBlock.values()) {
            plants.add(HFGathering.NATURE.getStackFromEnum(nature).getDisplayName());
        }

        for (FlowerType flower: FlowerType.values()) {
            if (flower != FlowerType.GODDESS) {
                plants.add(HFCore.FLOWERS.getStackFromEnum(flower).getDisplayName());
            }
        }

        addToBuilderAndSort("Plants & Mushrooms", plants, builder);

        List<String> junk = new ArrayList<>();
        for (Rock rock: Rock.values()) {
            junk.add(HFGathering.ROCK.getStackFromEnum(rock).getDisplayName());
        }

        for (Wood wood: Wood.values()) {
            junk.add(HFGathering.WOOD.getStackFromEnum(wood).getDisplayName());
        }

        builder.append("\n|-\n" +
                "!" + "Rocks and Logs" +"\n|");
        addToBuilderAndSort("Rocks and Logs", junk, builder, false);

        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:Gathering]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
        Debug.save(builder);
    }

    private void addToBuilderAndSort(String name, List<String> list, StringBuilder builder, boolean sort) {
        if (sort) {
            builder.append("|-\n" +
                    "!" + name +"\n|");
            Collections.sort(list, (o1, o2) -> {return o1.compareTo(o2); });
        }

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) builder.append(" • ");
            builder.append("[[" + list.get(i) + "]]");
        }
    }

    private void addToBuilderAndSort(String name, List<String> list, StringBuilder builder) {
        addToBuilderAndSort(name, list, builder, true);
    }
}

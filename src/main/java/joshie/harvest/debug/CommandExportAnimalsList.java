package joshie.harvest.debug;

import com.google.common.collect.Lists;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFCommand
public class CommandExportAnimalsList extends CommandExportHeld {
    @Override
    public String getCommandName() {
        return "export-animals-list";
    }

    @Override
    public boolean isExportable(ItemStack stack) {
        return true;
    }


    private static final String CAT1 = "Animal Care";
    private static final String CAT2 = "Animals";

    @Override
    protected void export(ItemStack stack, String... parameters) {
        StringBuilder builder = new StringBuilder();
        builder.append("<includeonly>{| class=\"wikitable\" id=\"navbox\"\n" +
                "! colspan=2 | [[" + CAT1 + "]]\n" +
                "|-\n" +
                "!" + CAT2 + "\n|");

        addToBuilderAndSort(Lists.newArrayList("Chicken", "Cow", "Sheep"), builder);
        builder.append("\n|-\n" +
                "!" + "Products" + "\n|");
        List<String> products = new ArrayList<>();
        for (Sizeable sizeable: Sizeable.values()) {
            products.add(WordUtils.capitalize(sizeable.getName()));
        }

        //Exceptions
        Collections.sort(products, (o1, o2) -> {return o1.compareTo(o2); });
        for (int i = 0; i < products.size(); i++) {
            if (i != 0) builder.append(" • ");

            builder.append("[[Small " + products.get(i) + "]]");
            builder.append(" • [[Medium " + products.get(i) + "]]");
            builder.append(" • [[Large " + products.get(i) + "]]");
        }

        builder.append("\n|-\n" +
                "!" + "Blocks" + "\n|");
        List<String> blocks = new ArrayList<>();
        blocks.add("Trough");
        blocks.add("Incubator");
        blocks.add("Feeding Tray");
        blocks.add("Nesting Box");
        addToBuilderAndSort(blocks, builder);

        builder.append("\n|-\n" +
                "!" + "Treats" + "\n|");
        List<String> treats = new ArrayList<>();
        for (Treat treat: Treat.values()) {
            treats.add(HFAnimals.TREATS.getStackFromEnum(treat).getDisplayName());
        }

        addToBuilderAndSort(treats, builder);
        builder.append("\n|-\n" +
                "!" + "Tools" + "\n|");
        List<String> tools = new ArrayList<>();
        tools.add("Shears");
        tools.add("Fodder");
        for (Tool tool: Tool.values()) {
            tools.add(HFAnimals.TOOLS.getStackFromEnum(tool).getDisplayName());
        }

        addToBuilderAndSort(tools, builder);
        builder.append("\n|-\n" +
        "|}{{mainonly|[[Category:" + CAT2+ "]][[Category:" + CAT1 + "]]}}</includeonly><noinclude>{{{{FULLPAGENAME}}/doc}}</noinclude>\n");
        Debug.save(builder);
    }

    private void addToBuilderAndSort(List<String> list, StringBuilder builder) {
        Collections.sort(list, (o1, o2) -> {return o1.compareTo(o2); });

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) builder.append(" • ");
            builder.append("[[" + list.get(i) + "]]");
        }
    }
}

package uk.joshiejack.penguinlib.command;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@PenguinLoader
public class CommandExport extends PenguinCommand {
    @Nonnull
    @Override
    public String getName() {
        return "export";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    private static void export(String name, List<Pair<String, String>> list) {
        try {
            StringBuilder csv = new StringBuilder();
            list.forEach(pair -> {
                csv.append("\"").append(pair.getLeft()).append("\"").append(",").append("\"").append(pair.getValue().replace("\"", "\"\"")).append("\"\n");
            });
            PrintWriter writer = new PrintWriter(name + "_database.csv", "UTF-8");
            writer.write(csv.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Exporter<I> {
        Pair<String, String> name(I i);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        export("item", StackHelper.getAllItems().stream().map(entry -> Pair.of(entry.getDisplayName(), StackHelper.getStringFromStack(entry))).collect(Collectors.toList()));
        export("entity", ForgeRegistries.ENTITIES.getValuesCollection().stream().map(entry -> Pair.of(entry.getName(), entry.getRegistryName().toString())).collect(Collectors.toList()));
        export("potion", ForgeRegistries.POTIONS.getValuesCollection().stream().map(entry -> Pair.of(entry.getName().toString(), entry.getRegistryName().toString())).collect(Collectors.toList()));
        //export("biome", ForgeRegistries.BIOMES.getValuesCollection().stream().map(entry -> Pair.of(entry.getBiomeName().toString(), entry.getRegistryName().toString())).collect(Collectors.toList()));
        //export("recipe", ForgeRegistries.RECIPES.getValuesCollection().stream().map(entry -> Pair.of(entry.getRecipeOutput().getDisplayName(), entry.getRegistryName().toString())).collect(Collectors.toList()));
    }
}

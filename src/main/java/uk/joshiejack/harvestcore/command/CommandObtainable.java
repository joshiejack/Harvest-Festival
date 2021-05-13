package uk.joshiejack.harvestcore.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.Level;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.harvestcore.tile.TileFurnace;
import uk.joshiejack.harvestcore.tile.TileKiln;
import uk.joshiejack.horticulture.tileentity.TileSeedMaker;
import uk.joshiejack.horticulture.tileentity.TileStump;
import uk.joshiejack.husbandry.animals.AnimalType;
import uk.joshiejack.husbandry.tile.TileFermenter;
import uk.joshiejack.husbandry.tile.TileHive;
import uk.joshiejack.husbandry.tile.TileOilMaker;
import uk.joshiejack.husbandry.tile.TileSpinningWheel;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.piscary.tile.TileRecyler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@PenguinLoader
public class CommandObtainable extends HCComand {
    @Nonnull
    @Override
    public String getName() {
        return "obtainable";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        for (ItemStack stack : StackHelper.getAllItems()) {
            if (stack.getItem().getRegistryName().getNamespace().equals("piscary")) {
                String ret = String.join(",", getLocation(sender.getEntityWorld(), stack));
                Level level = ret.contains("NOWHERE") ? Level.ERROR : ret.contains("VANILLA") ? Level.WARN : Level.INFO;
                PenguinLib.logger.log(level, (level != Level.INFO ? "WARNING!!! " : "") + stack.getDisplayName() + " can be obtained from " + ret);
            }
        }
    }

    public interface GetStack<T> {
        boolean matches(T t, Holder h);
    }

    private <T> void searchMachines(HolderRegistry<T> registry, GetStack<T> get, List<Holder> holders, String machine, List<String> list) {
        if (registry.values().stream().anyMatch(s -> {
            for (Holder h : holders) {
                if (get.matches(s, h)) return true;
            }

            return false;
        })) {
            list.add("from a " + machine);
        }
    }

    private List<String> getLocation(World world, ItemStack stack) {
        List<String> locations = Lists.newArrayList();
        HolderMeta meta = new HolderMeta(stack);
        //Search through COOKING RECIPES
        if (Recipe.RECIPE_BY_STACK.containsKey(meta)) locations.add("cooking with gastronomy");

        //Furnace Recipes
        if (FurnaceRecipes.instance().getSmeltingList().values().stream().anyMatch(output -> output.isItemEqual(stack))) {
            locations.add("cooking with a VANILLA furnace.");
        }

        //Crafting Recipes
        if (CraftingManager.REGISTRY.getKeys().stream().anyMatch(key -> {
            IRecipe recipe = CraftingManager.getRecipe(key);
            return recipe.getRecipeOutput().isItemEqual(stack);
        })) {
            locations.add("crafting with a VANILLA table.");
        }

        //Blueprints
        if (Blueprint.REGISTRY.values().stream().anyMatch(bp -> bp.getResult().isItemEqual(stack))) {
            locations.add("from a blueprint in harvestcore");
        }

        List<Holder> holders = Holder.getAsAll(stack);
        holders.removeIf(Objects::isNull);
        GetStack<ItemStack> matches = (s, h) -> s.isItemEqual(stack);
        searchMachines(TileFermenter.registry, matches, holders, "fermenter", locations);
        GetStack<TileFurnace.Recipe> r = (s, h) -> h.matches(s.getResult());
        searchMachines(TileFurnace.registry, r, holders, "furnace", locations);
        searchMachines(TileKiln.registry, matches, holders, "kiln", locations);
        searchMachines(TileSeedMaker.registry, matches, holders, "seed maker", locations);
        GetStack<TileStump.MushroomData> m = (s, h) -> h.matches(s.getResult());
        searchMachines(TileHive.registry, matches, holders, "hive", locations);
        searchMachines(TileOilMaker.registry, matches, holders, "oil maker", locations);
        searchMachines(TileSpinningWheel.registry, matches, holders, "spinning wheel", locations);
        searchMachines(TileRecyler.registry, matches, holders, "recycler", locations);

        if (AnimalType.TYPES.values().stream()
                .anyMatch(type -> IntStream.rangeClosed(0, 2)
                        .anyMatch(i -> type.getProducts().getProduct(i).isItemEqual(stack)))) {
            locations.add("an animal as a product");
        }

        LootTable table = world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
        List<LootPool> pools = ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
       // if (pools.stream().anyMatch(pool -> pool.get)) //TODO: Search loot tables, remember they can be nested so recursive?

        if (locations.isEmpty()) locations.add("FROM NOWHERE");
        return locations;
    }
}

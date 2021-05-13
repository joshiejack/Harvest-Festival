package uk.joshiejack.penguinlib.data.custom.material;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("tool_material:standard")
public class CustomToolMaterialData extends AbstractCustomData<CustomToolMaterialData, CustomToolMaterialData> {
    public static final Int2ObjectMap<CustomToolMaterialData> byInt = new Int2ObjectOpenHashMap<>();
    public static int MAX;
    public int harvestLevel;
    public int maxUses;
    public float efficiency;
    public float damage;

    @Nonnull
    @Override
    public CustomToolMaterialData build(ResourceLocation registryName, @Nonnull CustomToolMaterialData main, @Nullable CustomToolMaterialData... data) {
        main.name = registryName.getPath();
        byInt.put(main.harvestLevel, main);
        if (main.harvestLevel > MAX) {
            MAX = main.harvestLevel;
        }

        return main;
    }

    public CustomToolMaterialData next() {
        if (harvestLevel < MAX) return byInt.get(harvestLevel + 1);
        else return this;
    }
}

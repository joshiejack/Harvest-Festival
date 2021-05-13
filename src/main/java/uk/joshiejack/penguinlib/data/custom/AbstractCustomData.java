package uk.joshiejack.penguinlib.data.custom;

import joptsimple.internal.Strings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.oredict.OreDictionary;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public abstract class AbstractCustomData<B, A extends AbstractCustomData> {
    public transient String type;
    private transient ResourceLocation scriptID;
    public String name;
    public String script;

    @Nullable
    public ResourceLocation getScript() {
        if (scriptID == null && script != null) {
            scriptID = new ResourceLocation(script.replace("/", "_"));
        }

        return scriptID;
    }

    @Nonnull
    public abstract B build(ResourceLocation registryName, @Nonnull A main, @Nullable A... data);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCustomData that = (AbstractCustomData) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public abstract static class ItemOrBlock<B, A extends ItemOrBlock> extends AbstractCustomData<B, A> {
        private ModelResourceLocation model;
        public String[] ores;
        public int lifespan = -1;
        public int color = -1;

        public void init(@Nonnull ItemStack stack) {
            if (ores != null) {
                for (String ore : ores) {
                    if (!Strings.isNullOrEmpty(ore)) {
                        OreDictionary.registerOre(ore, stack);
                    }
                }
            }

            StackHelper.registerSynonym(stack.getItem().getRegistryName() + "#" + name, stack);
        }

        public ModelResourceLocation getModel(ResourceLocation registry, String prefix) {
            return model != null ? model: new ModelResourceLocation(Objects.requireNonNull(registry), prefix);
        }

        public int getColor(IBlockAccess world, BlockPos pos) {
            return color != 0 ? color : world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    }
}

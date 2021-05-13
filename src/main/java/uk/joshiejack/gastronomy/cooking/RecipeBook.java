package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

@SideOnly(Side.CLIENT)
public class RecipeBook {
    private static final RecipeBook INSTANCE = new RecipeBook();
    private Collection<HolderMeta> learnt = Sets.newHashSet();

    public static boolean learnRecipe(ItemStack stack) {
        return !INSTANCE.learnt.contains(new HolderMeta(stack));
    }

    public static void addLearntRecipe(HolderMeta meta) {
        INSTANCE.learnt.add(meta);
    }

    public static void setLearntRecipes(Collection<HolderMeta> holders) {
        INSTANCE.learnt = holders;
    }

    public static boolean hasLearntRecipe(Recipe recipe) {
        return INSTANCE.learnt.contains(new HolderMeta(recipe.getResult()));
    }
}

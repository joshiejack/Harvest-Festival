package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.fluid.GastronomyFluids;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import uk.joshiejack.penguinlib.util.handlers.SingleFluidConsumable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemIngredient extends ItemMulti<ItemIngredient.Ingredient> {
    public ItemIngredient() {
        super(new ResourceLocation(MODID, "ingredient"), Ingredient.class);
        setCreativeTab(Gastronomy.TAB);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return getEnumFromStack(stack) == Ingredient.COOKING_OIL ? new SingleFluidConsumable(stack, GastronomyFluids.COOKING_OIL) : null;
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(Ingredient ingredient) {
        return ingredient == Ingredient.DUMPLING_POWDER ? ItemStack.EMPTY : super.getCreativeStack(ingredient);
    }

    public enum Ingredient {
        CURRY_POWDER, DUMPLING_POWDER, FLOUR, SALT, COOKING_OIL
    }
 }

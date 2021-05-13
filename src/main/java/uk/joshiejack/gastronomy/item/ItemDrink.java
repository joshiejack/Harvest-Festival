package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.fluid.GastronomyFluids;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import uk.joshiejack.penguinlib.util.handlers.SingleFluidConsumable;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemDrink extends ItemMultiEdible<ItemDrink.Drink> {
    public ItemDrink() {
        super(new ResourceLocation(MODID, "drink"), Drink.class);
        setCreativeTab(Gastronomy.TAB);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return getEnumFromStack(stack) == Drink.WINE ? new SingleFluidConsumable(stack, GastronomyFluids.WINE) : null;
    }

    public enum Drink implements Edible {
        WINE(2, 0.8F), HOT_CHOCOLATE(4, 0.3F), KETCHUP(1, 0.8F),
        FRUIT_JUICE(5, 0.3F), MIX_JUICE(5, 0.4F), VEGETABLE_JUICE(4, 0.4F),
        FRUIT_LATTE(5, 0.4F), LATTE_MIX(5, 0.5F), VEGETABLE_LATTE(4, 0.5F);

        private final int hunger;
        private final float saturation;

        Drink(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        @Override
        public EnumAction getAction() {
            return EnumAction.DRINK;
        }

        @Override
        public int getHunger() {
            return hunger;
        }

        @Override
        public float getSaturation() {
            return saturation;
        }
    }
 }

package joshie.harvest.fishing.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.util.interfaces.ILength;
import joshie.harvest.fishing.item.ItemFish.Fish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

public class ItemFish extends ItemHFEnum<ItemFish, Fish> implements IShippable, ILength {
    public static final String SIZE = "Size";

    public ItemFish() {
        super(HFTab.FISHING, Fish.class);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public long getSellValue(ItemStack stack) {
        Fish fish = getEnumFromStack(stack);
        double weight = stack.hasTagCompound() ? stack.getTagCompound().getDouble(SIZE) : fish.getLengthFromSizeOfFish(SMALL_FISH);
        return fish.getSellValue(weight);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        Fish fish = getEnumFromStack(stack);
        double weight = stack.hasTagCompound() ? stack.getTagCompound().getDouble(SIZE) : fish.getLengthFromSizeOfFish(SMALL_FISH);
        tooltip.add("Length: " + weight + "cm");
    }

    @Override
    public double getLengthFromSizeOfFish(ItemStack stack, int size) {
        return getEnumFromStack(stack).getLengthFromSizeOfFish(size);
    }

    public static final int SMALL_FISH = 1;
    public static final int MEDIUM_FISH = 2;
    public static final int LARGE_FISH = 3;
    public static final int GIANT_FISH = 4;
    public enum Fish implements IStringSerializable{
        ANCHOVY(30L, 2D, 40D), ANGEL(230, 5D, 15D), ANGLER(500, 20D, 100D), BASS(105L, 35D, 75D), BLAASOP(365L, 34D, 110D), BOWFIN(130L, 50D, 109D),
        BUTTERFLY(200L, 12D, 22D), CARP(60L, 35D, 105D), CATFISH(120L, 100D, 250D), CHUB(40L, 40D, 80D), CLOWN(170L, 10D, 18D), COD(50L, 5D, 200D),
        DAMSEL(105L, 3D, 5D), ELECTRICRAY(230L, 80D, 190D), GOLD(35L, 5D, 45D), HERRING(85L, 14D, 46D), KOI(280L, 25D, 90D), LAMPREY(100L, 13D, 100D),
        LUNGFISH(200L, 70D, 150D), MANTARAY(400L, 400D, 700D), MINNOW(20L, 2D, 13D), PERCH(65L, 7.5D, 30D), PICKEREL(140L, 50D, 76D), PIKE(235L, 60D, 130D),
        PIRANHA(400L, 30D, 50D), PUFFER(300L, 2.5D, 61D), PUPFISH(115L, 5D, 8D), SALMON(80L, 60D, 80D), SARDINE(20L, 8D, 30D), SIAMESE(200L, 4D, 7D),
        STARGAZER(140L, 25D, 40D), STINGRAY(250L, 150D, 200D), TANG(230L, 20D, 35D), TETRA(185L, 1.5D, 4D), TROUT(80L, 25D, 90D), TUNA(160L, 40D, 460D),
        WALLEYE(110L, 25D, 35D);

        private final long sell;
        private final double small;
        private final double medium;
        private final double large;
        private final double giant;

        Fish(long sell, double min, double max) {
            this.sell = sell;
            this.small = min;
            this.medium = min + (max - min) * (1/3);
            this.large = this.medium * 2;
            this.giant = max;
        }

        public double getLengthFromSizeOfFish(int size) {
            if (size == SMALL_FISH) return small;
            else if (size == MEDIUM_FISH) return medium;
            else if (size == LARGE_FISH) return large;
            else if (size == GIANT_FISH) return giant;
            return small;
        }

        public long getSellValue(double size) {
            if (size >= giant) return (long)(sell * 1.75);
            if (size >= large) return (long)((double)sell * 1.5);
            else if (size >= medium) return (long)((double)sell * 1.25);
            else return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

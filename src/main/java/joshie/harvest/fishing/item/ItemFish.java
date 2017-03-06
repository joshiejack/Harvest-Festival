package joshie.harvest.fishing.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.fishing.item.ItemFish.Fish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

import static joshie.harvest.api.calendar.Season.*;

public class ItemFish extends ItemHFFoodEnum<ItemFish, Fish> {
    public static final Multimap<Season, Fish> FISH_LOCATIONS = HashMultimap.create();
    public static final String SIZE = "Size";

    public ItemFish() {
        super(HFTab.FISHING, Fish.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("ConstantConditions")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        Fish fish = getEnumFromStack(stack);
        double weight = stack.hasTagCompound() ? stack.getTagCompound().getDouble(SIZE) : fish.getLengthFromSizeOfFish(SMALL_FISH);
        tooltip.add("Length: " + weight + "cm");
    }

    public double getLengthFromSizeOfFish(ItemStack stack, int size) {
        return getEnumFromStack(stack).getLengthFromSizeOfFish(size);
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return getEnumFromStack(stack).getFoodAmount();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return 0.6F;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (getEnumFromStack(stack).isPoisonous()) {
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 1200, 3));
            player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 300, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 1));
        }

        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.LAST;
    }

    public static final int SMALL_FISH = 1;
    public static final int MEDIUM_FISH = 2;
    public static final int LARGE_FISH = 3;
    public static final int GIANT_FISH = 4;
    public enum Fish implements IStringSerializable, ISellable {
        ANCHOVY(30L, 2D, 40D, SPRING, SUMMER, AUTUMN, WINTER), ANGEL(230, 5D, 15D, SPRING), ANGLER(500, 20D, 100D, WINTER), BASS(105L, 35D, 75D, SPRING, AUTUMN), BLAASOP(365L, 34D, 110D, WINTER),
        BOWFIN(130L, 50D, 109D, SPRING, SUMMER, AUTUMN, WINTER), BUTTERFLY(200L, 12D, 22D, SPRING, SUMMER), CARP(60L, 35D, 105D, SPRING, SUMMER, AUTUMN, WINTER), CATFISH(120L, 100D, 250D, SUMMER, AUTUMN, WINTER),
        CHUB(40L, 40D, 80D, SPRING, SUMMER, AUTUMN, WINTER), CLOWN(170L, 10D, 18D, SPRING, SUMMER), COD(50L, 5D, 200D, SPRING, AUTUMN, WINTER), DAMSEL(105L, 3D, 5D, SPRING, SUMMER), ELECTRICRAY(230L, 80D, 190D, AUTUMN, WINTER),
        GOLD(35L, 5D, 45D, SPRING, SUMMER, AUTUMN, WINTER), HERRING(85L, 14D, 46D, SPRING, AUTUMN, WINTER), KOI(280L, 25D, 90D, SUMMER, WINTER), LAMPREY(100L, 13D, 100D, SPRING, AUTUMN, WINTER), LUNGFISH(200L, 70D, 150D, SUMMER),
        MANTARAY(400L, 400D, 700D, SPRING), MINNOW(20L, 2D, 13D, SPRING, SUMMER, AUTUMN, WINTER), PERCH(65L, 7.5D, 30D, SPRING, SUMMER, AUTUMN, WINTER), PICKEREL(140L, 50D, 76D, SPRING, SUMMER, WINTER), PIKE(235L, 60D, 130D, SUMMER, WINTER),
        PIRANHA(400L, 30D, 50D, SUMMER), PUFFER(300L, 2.5D, 61D, SUMMER), PUPFISH(115L, 5D, 8D, SPRING, SUMMER, AUTUMN, WINTER), SALMON(80L, 60D, 80D, SPRING, SUMMER, AUTUMN, WINTER), SARDINE(20L, 8D, 30D, SUMMER, AUTUMN, WINTER),
        SIAMESE(200L, 4D, 7D, SPRING, WINTER), STARGAZER(140L, 25D, 40D, SPRING, SUMMER), STINGRAY(250L, 150D, 200D, SPRING, SUMMER), TANG(230L, 20D, 35D, SPRING, SUMMER), TETRA(185L, 1.5D, 4D, SPRING, SUMMER),
        TROUT(80L, 25D, 90D, SPRING, SUMMER, AUTUMN, WINTER), TUNA(160L, 40D, 460D, AUTUMN, WINTER), WALLEYE(110L, 25D, 35D, SUMMER, AUTUMN);

        private final long sell;
        private final double small;
        private final double medium;
        private final double large;
        private final double giant;
        private final int amount;

        Fish(long sell, double min, double max, Season... seasons) {
            this.sell = sell;
            this.small = min;
            this.medium = min + (max - min) * (1/3);
            this.large = this.medium * 2;
            this.giant = max;
            this.amount = (int) Math.min(10, Math.max(1, min / 10D));
            for (Season season: seasons) {
                ItemFish.FISH_LOCATIONS.get(season).add(this);
            }
        }

        public boolean isPoisonous() {
            return this == BLAASOP || this == LAMPREY || this == PUFFER || this == STARGAZER || this == STINGRAY;
        }

        public boolean isUncookable() {
            return isPoisonous() || this == TETRA || this == CLOWN || this == BUTTERFLY || this == ANGEL || this == SIAMESE || this == TANG;
        }

        public int getFoodAmount() {
            return amount;
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
        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.Size;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeableHelper {
    public static ItemStack getProduct(EntityPlayer player, AnimalStats stats) {
        Size size = null;
        int relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(EntityHelper.getEntityUUID(stats.getAnimal()));
        for (Size s : Size.values()) {
            if (relationship >= s.getRelationshipRequirement()) size = s;
        }

        return HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.EGG, size);
    }

    public static ItemStack getMilk(AnimalStats stats) {
        return SizeableHelper.getSizeable(stats, Sizeable.MILK);
    }

    public static ItemStack getWool(AnimalStats stats) {
        return SizeableHelper.getSizeable(stats, Sizeable.WOOL);
    }

    private static ItemStack getSizeable(AnimalStats stats, Sizeable sizeable) {
        return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, getSizeFromAnimal(stats.getHappiness(), stats.getAnimal()), stats.getProductsPerDay());
    }

    public static Size getSizeFromAnimal(int happiness, EntityAnimal animal) {
        WeightedSize weighted = new WeightedSize();
        for (Size size: Size.values()) {
            int value = 27000 -(27000 -(happiness - size.getRelationshipRequirement()));
            if (value > 0) {
                weighted.add(size, value);
            }
        }

        return weighted.get(animal);
    }

    private static class WeightedSize {
        private final NavigableMap<Double, Size> map = new TreeMap<>();
        private double total = 0;
        public void add(Size size, double weight) {
            if (weight > 0) {
                total += weight;
                map.put(total, size);
            }
        }

        @Nullable
        public Size get(EntityAnimal animal) {
            if (map.size() == 0) return Size.SMALL;
            return map.ceilingEntry((animal.worldObj.rand.nextDouble() * total)).getValue();
        }
    }
}

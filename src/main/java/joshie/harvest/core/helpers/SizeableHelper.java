package joshie.harvest.core.helpers;

import java.util.Random;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFItems;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SizeableHelper {
    private static final Random rand = new Random();

    public static ItemStack getEgg(EntityPlayer player, EntityAnimal animal) {
        Size size = Size.SMALL;
        int relationship = RelationsHelper.getRelationshipValue(animal, player);
        int chance = Math.max(1, RelationsHelper.ADJUSTED_MAX - relationship);
        int chance2 = Math.max(1, chance / 3);
        if (rand.nextInt(chance) == 0) size = Size.LARGE;
        else if (rand.nextInt(chance2) == 0) size = Size.MEDIUM;
        ItemStack sizeable = getSizeable(relationship, SizeableMeta.EGG, size);
        if (HFConfig.vanilla.EGG_OVERRIDE) {
            return new ItemStack(Items.egg, 1, sizeable.getItemDamage());
        } else return sizeable;
    }

    public static ItemStack getSizeable(EntityPlayer player, EntityAnimal animal, SizeableMeta meta, Size size) {
        int relationship = RelationsHelper.getRelationshipValue(animal, player);
        return getSizeable(relationship, meta, size);
    }

    public static ItemStack getSizeable(int relationship, SizeableMeta meta, Size size) {
        return new ItemStack(HFItems.sized.get(meta), 1, size.ordinal());
    }

    public static Size getSize(int meta) {
        int size = Math.max(0, meta);
        return Size.values()[Math.min(2, meta)];
    }
}

package joshie.harvest.mining.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class ItemMaterial extends ItemHFEnum<ItemMaterial, Material> {
    public enum Material implements IStringSerializable, ISellable {
        JUNK(1L), COPPER(15L), SILVER(20L), GOLD(25L), MYSTRIL(40L), MYTHIC(20000L),
        ADAMANTITE(50L), AGATE(62L), ALEXANDRITE(10000L), AMETHYST(60L), FLUORITE(20L),
        MOON_STONE(25L), ORICHALC(50L), PERIDOT(35L), PINK_DIAMOND(10000L), RUBY(75L),
        SAND_ROSE(30L), TOPAZ(70L);

        private final long sell;

        Material(long sell) {
            this.sell = sell;
        }

        public boolean isUpgrade() {
            return ordinal() >= COPPER.ordinal() && ordinal() <= MYTHIC.ordinal();
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

    public ItemMaterial() {
        super(HFTab.MINING, Material.class);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case MYTHIC:
                return GREEN + super.getItemStackDisplayName(stack);
            default:
                return WHITE + super.getItemStackDisplayName(stack);
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.NONE + stack.getItemDamage();
    }
}
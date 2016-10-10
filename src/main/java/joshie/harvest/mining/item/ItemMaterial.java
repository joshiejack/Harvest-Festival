package joshie.harvest.mining.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class ItemMaterial extends ItemHFEnum<ItemMaterial, Material> implements IShippable {
    public enum Material implements IStringSerializable {
        JUNK(1L), COPPER(15L), SILVER(20L),  GOLD(25L), MYSTRIL(40L), MYTHIC(20000L),
        ADAMANTITE(50L), AGATE(62L), ALEXANDRITE(10000L), AMETHYST(60L), FLUORITE(65L),
        MOON_STONE(55L), ORICHALC(50L), PERIDOT(68L), PINK_DIAMOND(10000L), RUBY(75L),
        SAND_ROSE(60L), TOPAZ(70L);

        private final long sell;

        Material(long sell) {
            this.sell = sell;
        }

        public boolean isUpgrade() {
            return ordinal() >= COPPER.ordinal() && ordinal() <= MYTHIC.ordinal();
        }

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
    public long getSellValue(ItemStack stack) {
        return getEnumFromStack(stack).getSellValue();
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromStack(stack).isUpgrade()) {
            list.add(TextHelper.translate("tooltip.mythic_stone"));
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 40 + stack.getItemDamage();
    }
}
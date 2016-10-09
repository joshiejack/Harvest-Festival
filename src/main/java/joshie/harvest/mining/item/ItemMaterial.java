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

import static joshie.harvest.mining.item.ItemMaterial.Material.MYTHIC;
import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class ItemMaterial extends ItemHFEnum<ItemMaterial, Material> implements IShippable {
    public enum Material implements IStringSerializable {
        JUNK, COPPER, SILVER, GOLD, MYSTRIL, MYTHIC;

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
        switch (getEnumFromStack(stack)) {
            case JUNK: return 1;
            case COPPER: return 15;
            case SILVER: return 20;
            case GOLD: return 35;
            case MYSTRIL: return 40;
            case MYTHIC: return 20000;
            default: return 0;
        }
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
        if (getEnumFromStack(stack) == MYTHIC) {
            list.add(TextHelper.translate("tooltip.mythic_stone"));
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 40 + stack.getItemDamage();
    }
}
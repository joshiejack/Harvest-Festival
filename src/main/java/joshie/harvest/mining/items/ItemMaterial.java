package joshie.harvest.mining.items;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFEnum;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.mining.items.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.mining.items.ItemMaterial.Material.MYTHIC;
import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;

public class ItemMaterial extends ItemHFEnum<ItemMaterial, Material> {
    public enum Material implements IStringSerializable {
        JUNK, COPPER, SILVER, GOLD, MYSTRIL, MYTHIC;

        @Override
        public String getName() {
            return name().toLowerCase();
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (getEnumFromStack(stack) == MYTHIC) {
            list.add(Text.translate("tooltip.mythic_stone"));
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 40 + stack.getItemDamage();
    }
}
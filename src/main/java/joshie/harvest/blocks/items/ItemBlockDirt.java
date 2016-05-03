package joshie.harvest.blocks.items;

import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemBlockHFBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockDirt extends ItemBlockHFBase {
    public ItemBlockDirt(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return "mine_floor";
    }

    /*
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if (General.DEBUG_MODE) {
            if (stack.getItemDamage() < FloorType.values().length) {
                list.add(Text.INDIGO + Text.ITALIC + WordUtils.capitalizeFully(FloorType.values()[stack.getItemDamage()].name().replace("_", " ")));
            }
        }
    }
    */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == 1) list.add(Translate.translate("tooltip.dirt"));
    }
}
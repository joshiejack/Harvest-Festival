package joshie.harvest.blocks.items;

import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemBlockHFBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockStone extends ItemBlockHFBase {
    public ItemBlockStone(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        return "mine_wall";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack.getItemDamage() == 1) list.add(Translate.translate("tooltip.dirt"));
    }
}
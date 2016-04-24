package joshie.harvest.blocks.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.BlockFlower;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemBlockBase;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockFlower extends ItemBlockBase implements ICreativeSorted {
    public ItemBlockFlower(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockFlower.GODDESS:
                return "goddess";
            default:
                return "invalid";
        }
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return stack.getItemDamage() == BlockFlower.GODDESS ? 60 : super.getEntityLifespan(stack, world);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.AQUA + super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
        list.add(Translate.translate("tooltip.flower"));
    }
}

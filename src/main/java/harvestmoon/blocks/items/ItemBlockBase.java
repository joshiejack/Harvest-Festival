package harvestmoon.blocks.items;

import harvestmoon.util.Text;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class ItemBlockBase extends ItemBlock {
    public ItemBlockBase(Block block) {
        super(block);
    }
    
    @Override
    public int getMetadata(int meta) {
        return meta;
    }
    
    public abstract String getName(int dmg);
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = field_150939_a.getUnlocalizedName().replace("tile.", "");
        return Text.localize(unlocalized + "." + getName(stack.getItemDamage()));
    }
}

package joshie.harvest.blocks.items;

import joshie.harvest.buildings.Building;
import joshie.harvest.core.util.ICreativeSorted;
import joshie.harvest.core.util.base.ItemBlockBase;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.init.HFItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPreview extends ItemBlockBase implements ICreativeSorted {    
    public ItemBlockPreview(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 1;
    }

    @Override
    public int getMetadata(int meta) {
        return 0;
    }
        
    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= Building.buildings.size()) return "invalid";
        Building group = Building.buildings.get(stack.getItemDamage());
        if (group != null) {
            return group.getName();
        } else return "invalid";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = field_150939_a.getUnlocalizedName().replace("preview", "structures").replace("tile.", "").replace("_", ".");
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(unlocalized + "." + name.replace("_", "."));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return HFItems.structures.getIconFromDamage(damage);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 105;
    }
}

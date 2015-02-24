package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.buildings.BuildingGroup;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockPreview extends ItemBlockBase {
    public ItemBlockPreview(Block block) {
        super(block);
    }
    
    @Override
    public int getMetadata(int meta) {
        return 0;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= BuildingGroup.groups.size()) return "invalid";
        BuildingGroup group = BuildingGroup.groups.get(stack.getItemDamage());
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
}

package joshie.harvest.items;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.Translate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;

public class ItemBaseSingle extends Item {
    protected String path = MODPATH + ":";

    public ItemBaseSingle() {
        setCreativeTab(HFTab.FARMING);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        GameRegistry.register(this, new ResourceLocation(HFModInfo.MODID, name.replace(".", "_")));
        return this;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Translate.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}
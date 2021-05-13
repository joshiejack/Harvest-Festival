package uk.joshiejack.penguinlib.item.base.block;

import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBlockSingular extends ItemBlock implements IPenguinItem {
    private final String prefix;
    private final IPenguinBlock penguin;

    public ItemBlockSingular(ResourceLocation registry, IPenguinBlock block) {
        super((Block) block);
        prefix = registry.getNamespace() + ".block." + registry.getPath();
        penguin = block;
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            block.getSubBlocks(tab, items);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        penguin.registerModels(this);
    }
}

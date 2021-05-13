package uk.joshiejack.penguinlib.item.custom;

import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBlockSingularCustom extends ItemBlock implements IPenguinItem {
    private final AbstractCustomData.ItemOrBlock data;
    private final String prefix;
    private final IPenguinBlock penguin;

    public ItemBlockSingularCustom(ResourceLocation registry, IPenguinBlock block, AbstractCustomData.ItemOrBlock data) {
        super((Block) block);
        this.penguin = block;
        this.data = data;
        prefix = registry.getNamespace() + ".block." + registry.getPath();
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return data.lifespan == -1 ? 6000 : data.lifespan;
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

package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemSingularTool extends ItemTool implements IPenguinItem {
    private final String prefix;

    public ItemSingularTool(ResourceLocation registry, ToolMaterial material, Set<Block> effective) {
        super(material, effective);
        prefix = registry.getNamespace() + ".item." + registry.getPath();
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
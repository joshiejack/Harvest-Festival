package joshie.harvest.core.base.block;

import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public abstract class BlockHFBase<B extends BlockHFBase> extends Block {
    private String unlocalizedName;

    //General Constructor
    public BlockHFBase(Material material, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
    }

    @SuppressWarnings("unchecked")
    public B register(String name) {
        this.unlocalizedName = MODID + "." + name.replace("_", ".");
        setTranslationKey(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameData.register_impl(this);
        ItemBlockHF item = getItemBlock();
        if (item != null) item.register(name);
        return (B) this;
    }

    public ItemBlockHF getItemBlock() {
        return new ItemBlockHF<>(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public B setBlockUnbreakable() {
        return (B) super.setBlockUnbreakable();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public B setHardness(float hardness) {
        return (B) super.setHardness(hardness);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public B setResistance(float value) {
        return (B) super.setResistance(value);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public String getTranslationKey(@Nonnull ItemStack stack) {
        return getTranslationKey();
    }

    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        String unlocalized = getTranslationKey();
        String name = stack.getItem().getTranslationKey(stack);
        return TextHelper.localizeFully(unlocalized + "." + name);
    }

    public int getEntityLifeSpan(@Nonnull ItemStack itemStack, World world) {
        return 6000;
    }

    public int getSortValue(@Nonnull ItemStack stack) {
        return 0;
    }

    @Override
    @Nonnull
    public String getTranslationKey() {
        return unlocalizedName;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, name), "inventory"));
    }
}
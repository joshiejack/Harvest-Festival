package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
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

public abstract class BlockHFBase<B extends BlockHFBase> extends Block {
    private String unlocalizedName;

    //General Constructor
    public BlockHFBase(Material material, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
    }

    //Default to farming constructor
    public BlockHFBase(Material material) {
        this(material, HFTab.FARMING);
    }

    @Override
    public B setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerBlock(this, name);
        this.unlocalizedName = HFModInfo.MODID + "." + name;
        return (B) this;
    }

    @Override
    public B setBlockUnbreakable() {
        super.setBlockUnbreakable();
        return (B) this;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = getUnlocalizedName();
        String name = stack.getItem().getUnlocalizedName(stack);
        return Text.localizeFully(unlocalized + "." + name);
    }

    public int getEntityLifeSpan(ItemStack itemStack, World world) {
        return 6000;
    }

    public int getSortValue(ItemStack stack) {
        return 0;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, name), "inventory"));
    }
}
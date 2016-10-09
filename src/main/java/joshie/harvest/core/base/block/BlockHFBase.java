package joshie.harvest.core.base.block;

import joshie.harvest.core.HFTab;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.core.lib.HFModInfo.MODID;

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

    @SuppressWarnings("unchecked")
    public B register(String name) {
        this.unlocalizedName = MODID + "." + name.replace("_", ".");
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        ItemBlockHF item = getItemBlock();
        if (item != null) item.register(name);
        return (B) this;
    }

    public ItemBlockHF getItemBlock() {
        return new ItemBlockHF(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public B setBlockUnbreakable() {
        return (B) super.setBlockUnbreakable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public B setHardness(float hardness) {
        return (B) super.setHardness(hardness);
    }

    @SuppressWarnings("unchecked")
    @Override
    public B setResistance(float value) {
        return (B) super.setResistance(value);
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
        return TextHelper.localizeFully(unlocalized + "." + name);
    }

    public int getEntityLifeSpan(ItemStack itemStack, World world) {
        return 6000;
    }

    public int getSortValue(ItemStack stack) {
        return 0;
    }

    public long getSellValue(ItemStack stack) {
        return 0L;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(MODID, name), "inventory"));
    }
}
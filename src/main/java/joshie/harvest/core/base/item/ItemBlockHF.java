package joshie.harvest.core.base.item;

import joshie.harvest.core.base.block.BlockHFBase;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemBlockHF extends ItemBlock implements ICreativeSorted, IShippable {
    private final BlockHFBase block;

    public ItemBlockHF(BlockHFBase block) {
        super(block);
        this.block = block;
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return block.getItemStackDisplayName(stack);
    }

    @Override
    public BlockHFBase getBlock() {
        return block;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)  {
        return block.getEntityLifeSpan(itemStack, world);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return block.getUnlocalizedName(stack);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return block.getSortValue(stack);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return block.getSellValue(stack);
    }

    public void register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            block.registerModels(this, name);
        }
    }
}
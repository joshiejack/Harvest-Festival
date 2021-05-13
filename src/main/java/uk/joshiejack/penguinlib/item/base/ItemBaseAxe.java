package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.ImmutableSet;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public abstract class ItemBaseAxe extends ItemAxe implements IPenguinItem {
    private static final String toolClass = "lumber_axe";
    private final String prefix;

    public ItemBaseAxe(ResourceLocation registry, float effiency, float attack) {
        super(ToolMaterial.DIAMOND);
        this.prefix = registry.getNamespace() + ".item." + registry.getPath();
        this.efficiency = effiency;
        this.attackDamage = 8F + attack;
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Nonnull
    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(ItemBaseAxe.toolClass);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return stack.getItemDamage() < stack.getMaxDamage() && toolClass.equals(ItemBaseAxe.toolClass) ? getToolLevel() : -1;
    }

    protected abstract int getToolLevel();

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state)  {
        return stack.getItemDamage() < stack.getMaxDamage() ? super.getDestroySpeed(stack, state) : 1F;
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EntityLivingBase entityLiving)  {
        if (!worldIn.isRemote && (double)state.getBlockHardness(worldIn, pos) != 0.0D && stack.getItemDamage() < stack.getMaxDamage()) {
            stack.damageItem(1, entityLiving);
        }

        return true;
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
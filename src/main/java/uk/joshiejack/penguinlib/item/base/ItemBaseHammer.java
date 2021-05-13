package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.item.tools.AreaOfEffect;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public abstract class ItemBaseHammer extends ItemPickaxe implements IPenguinItem {
    protected static final Int2ObjectMap<AreaOfEffect> aoeByHarvestLevel = new Int2ObjectOpenHashMap<>();
    private final String prefix;
    private static final String toolClass = "hammer";

    public ItemBaseHammer(ResourceLocation registry, float effiency, float attack) {
        super(ToolMaterial.STONE);
        this.prefix = registry.getNamespace() + ".item." + registry.getPath();
        this.efficiency = effiency;
        this.attackDamage = 8F + attack;
        this.attackSpeed = -3.8F;
        aoeByHarvestLevel.put(0, AreaOfEffect.SINGULAR);
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return stack.getItemDamage() == stack.getMaxDamage() ?
                StringHelper.format("penguinlib.item.tools.broken", super.getItemStackDisplayName(stack)) : super.getItemStackDisplayName(stack);
    }

    protected abstract int getToolLevel();

    @Override
    public int getHarvestLevel(ItemStack stack, @Nonnull String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return stack.getItemDamage() < stack.getMaxDamage() && (toolClass.equals(ItemBaseHammer.toolClass) || toolClass.equals("pickaxe")) ? getToolLevel() : -1;
    }

    @Nonnull
    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(ItemBaseHammer.toolClass, "pickaxe");
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state)  {
        return stack.getItemDamage() < stack.getMaxDamage() ? super.getDestroySpeed(stack, state) : 1F;
    }

    @Nonnull
    public int getChargeLevel(ItemStack stack) { return 0; }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World world, @Nonnull IBlockState state, @Nonnull BlockPos position, @Nonnull EntityLivingBase entityLiving) {
        return entityLiving instanceof EntityPlayer && aoeByHarvestLevel.get(getChargeLevel(stack)).onBlockDestroyed(stack, world, position, (EntityPlayer) entityLiving);
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
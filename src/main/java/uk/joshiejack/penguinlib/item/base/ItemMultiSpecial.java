package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItemMap;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class ItemMultiSpecial<E extends IPenguinItemMap<E>> extends ItemBlockSpecial implements IPenguinItem {
    private final Map<ResourceLocation, E> map;
    private final String name;

    public ItemMultiSpecial(ResourceLocation resource, Map<ResourceLocation, E> registry) {
        super(Blocks.AIR);
        map = registry;
        name = resource.getNamespace() + ".item." + resource.getPath();
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(resource, this);
    }

    protected abstract E getNullEntry();

    public E getObjectFromStack(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Resource")) {
            return getNullEntry();
        } else {
            ResourceLocation resource = new ResourceLocation(stack.getTagCompound().getString("Resource"));
            E e = map.get(resource);
            return e != null ? e : getNullEntry();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public ItemStack getStackFromResource(ResourceLocation resource) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Resource", resource.toString());
        return stack;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public abstract IBlockState getStateForPlacement(E e);

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.SNOW_LAYER && iblockstate.getValue(BlockSnow.LAYERS) < 1) {
            facing = EnumFacing.UP;
        } else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);
        E value = getObjectFromStack(itemstack);
        IBlockState theBlock = getStateForPlacement(value);
        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(theBlock.getBlock(), pos, false, facing, null)) {
            IBlockState iblockstate1 = theBlock;
            if (!worldIn.setBlockState(pos, iblockstate1, 11)) {
                return EnumActionResult.FAIL;
            } else {
                iblockstate1 = worldIn.getBlockState(pos);

                if (iblockstate1.getBlock() == theBlock.getBlock()) {
                    ItemBlock.setTileEntityNBT(worldIn, player, pos, itemstack);
                    iblockstate1.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, player, itemstack);
                    onBlockPlacedBy(worldIn, pos, iblockstate1, player, itemstack, value);

                    if (player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);
                    }
                }

                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }

    protected abstract void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState iblockstate1, EntityPlayer player, ItemStack itemstack, E value);

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName();
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromResource(object.getRegistryName());
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (E entry : map.values()) {
                if (entry == getNullEntry()) continue;
                ItemStack stack = getCreativeStack(entry);
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        map.values().stream().filter(f -> f.getItemModelLocation() != null).forEach(v -> ModelBakery.registerItemVariants(this, v.getItemModelLocation()));
        ModelLoader.setCustomMeshDefinition(this, stack -> getObjectFromStack(stack).getItemModelLocation());
    }
}

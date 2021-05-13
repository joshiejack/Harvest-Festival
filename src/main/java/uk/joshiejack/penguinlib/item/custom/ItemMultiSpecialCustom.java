package uk.joshiejack.penguinlib.item.custom;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItemMulti;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class ItemMultiSpecialCustom<A extends AbstractCustomData.ItemOrBlock> extends ItemBlockSpecial implements IPenguinItem, ICustomItemMulti {
    private final Object2IntMap<A> ids = new Object2IntOpenHashMap<>();
    private final Map<String, A> byName = Maps.newHashMap();
    private final A defaults;
    private final A[] data;
    private final String prefix;

    public ItemMultiSpecialCustom(ResourceLocation registry, A defaults, A... data) {
        super(Blocks.AIR);
        this.defaults = defaults;
        this.data = data;
        for (int i = 0; i < this.data.length; i++) {
            ids.put(this.data[i], i);
            byName.put(this.data[i].name, this.data[i]);
        }

        prefix = registry.getNamespace() + ".item." + registry.getPath() + ".";
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public void init() {
        for (A cd: data) {
            cd.init(getStackFromData(cd)); //Init the data, and then... register synonymns
            StackHelper.registerSynonym(getRegistryName() + "#" + cd.name, getCreativeStack(cd));
        }
    }

    @Override
    public A getDefaults() {
        return defaults;
    }

    @Override
    public A[] getStates() {
        return data;
    }

    public ItemStack getStackFromString(String name) {
        return getStackFromData(byName.get(name));
    }

    @Override
    public ItemStack getStackFromString(String name, int count) {
        return getStackFromData(byName.get(name), count);
    }

    @Override
    public A getDataFromStack(ItemStack stack) {
        return getDataFromMeta(stack.getItemDamage());
    }

    private A getDataFromMeta(int meta) {
        return (A) ArrayHelper.getArrayValue(data, meta);
    }

    @Nonnull
    public ItemStack getStackFromData(A e, int size) {
        return new ItemStack(this, size, ids.get(e));
    }

    public ItemStack getStackFromData(A e) {
        return getStackFromData(e, 1);
    }

    public abstract IBlockState getStateForPlacement(A e);

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
        A value = getDataFromStack(itemstack);
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

    protected abstract void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState iblockstate1, EntityPlayer player, ItemStack itemstack, A value);

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return prefix + getDataFromStack(stack).name;
    }

    @Nonnull
    protected ItemStack getCreativeStack(A string) {
        return getStackFromData(string, 1);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (A string : data) {
                ItemStack stack = getCreativeStack(string);
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        for (A cd : data) {
            ModelLoader.setCustomModelResourceLocation(this, ids.get(cd), cd.getModel(getRegistryName(), cd.name));
        }
    }
}

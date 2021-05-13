package uk.joshiejack.penguinlib.block.custom;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.block.ICustomBlock;
import uk.joshiejack.penguinlib.data.custom.PropertyCustomData;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.item.custom.ItemBlockMultiCustom;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.LootHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMultiBushCustom extends BlockBush implements IPenguinBlock, ICustomBlock {
    protected final BiMap<AbstractCustomBlockData, Integer> ids = HashBiMap.create();
    protected static PropertyCustomData temporary;
    public final PropertyCustomData property;
    protected final AbstractCustomBlockData[] data;
    protected final AbstractCustomBlockData defaults;
    private final String propertyName;

    //Main Constructor
    @SuppressWarnings("unchecked")
    public BlockMultiBushCustom(ResourceLocation registry, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        super(preInit(defaults.propertyName, defaults, data));
        propertyName = defaults.propertyName;
        property = temporary;
        //byName = byNameTemporary;
        this.data = data;
        this.defaults = defaults;
        setDefaultState(blockState.getBaseState());
        for (int i = 0; i < this.data.length; i++) {
            ids.put(this.data[i], i);
        }

        setHardness(defaults.hardness);
        setSoundType(defaults.soundType == null ? SoundType.PLANT : defaults.soundType);
        RegistryHelper.registerBlock(registry, this);
    }

    @Nonnull
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        SoundType type = getDataFromState(state).soundType;
        return type == null ? super.getSoundType(state, world, pos, entity) : type;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return getBlockHardness(state, entity.world, pos) != -1F;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        float explosionResistance = getDataFromState(world.getBlockState(pos)).explosionResistance;
        return explosionResistance == -10F? defaults.explosionResistance == -10F?
                super.getExplosionResistance(world, pos, exploder, explosion) : defaults.explosionResistance : explosionResistance;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        float hardness = getDataFromState(blockState).hardness;
        return hardness == -10F ? super.getBlockHardness(blockState, worldIn, pos) : hardness;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        Boolean silkHarvest = getDataFromState(state).silkHarvest;
        return silkHarvest == null ? defaults.silkHarvest == null ? false : defaults.silkHarvest : silkHarvest;
    }

    @Override
    public boolean isToolEffective(String type, @Nonnull IBlockState state) {
        return getDataFromState(state).toolType == null && defaults.toolType == null ? super.isToolEffective(type, state) : getDataFromState(state).getToolTypes(defaults).contains(type);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        BlockRenderLayer renderLayer = getDataFromState(state).renderLayer;
        return renderLayer == null ? defaults.renderLayer == null ? layer == getRenderLayer() : layer == defaults.renderLayer : layer == renderLayer;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB aabb = getDataFromState(state).boundingBox;
        return aabb == null ? defaults.boundingBox == null ? super.getBoundingBox(state, source, pos) : defaults.boundingBox : aabb;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        AxisAlignedBB aabb = getDataFromState(state).collisionBox;
        return aabb == null ? defaults.collisionBox == null ? super.getCollisionBoundingBox(state, worldIn, pos) : defaults.collisionBox : aabb;
    }

    @Override
    public Material getMaterial(IBlockState state) {
        Material material = getDataFromState(state).material;
        return material == null ? super.getMaterial(state) : material;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        AbstractCustomBlockData data = getDataFromState(state);
        ResourceLocation script = data.script != null ? data.getScript() : defaults.getScript();
        if (script != null) {
            Scripting.callFunction(script, "onEntityCollision", world, pos, state, entity);        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        ResourceLocation lootTable = getDataFromState(state).lootTable;
        if (lootTable != null && world instanceof WorldServer) {
            drops.addAll(LootHelper.getLoot(lootTable, (World) world, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos), fortune));
        }
    }

    private static Material preInit(String property, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        temporary = new PropertyCustomData(property, data);
        return defaults.material == null ? Material.PLANTS : defaults.material;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null) return new BlockStateContainer(this, temporary);
        return new BlockStateContainer(this, property);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, getDataFromMeta(meta));
    }

    public IBlockState getStateFromData(AbstractCustomBlockData e) {
        return getDefaultState().withProperty(property, e);
    }

    public AbstractCustomData getEnumFromBlockPos(IBlockAccess world, BlockPos pos) {
        return getDataFromState(world.getBlockState(pos));
    }

    @Override
    public AbstractCustomBlockData getDefaults() {
        return defaults;
    }

    @Override
    public AbstractCustomBlockData[] getStates() {
        return data;
    }

    @Override
    public AbstractCustomBlockData getDataFromState(IBlockState state) {
        return state.getValue(property);
    }

    public AbstractCustomBlockData getDataFromMeta(int meta) {
        return ArrayHelper.getArrayValue(data, meta);
    }

    public AbstractCustomData getDataFromStack(ItemStack stack) {
        return getDataFromMeta(stack.getItemDamage());
    }

    public ItemStack getStackFromData(AbstractCustomData e) {
        return getStackFromData(e, 1);
    }

    public ItemStack getStackFromData(AbstractCustomData e, int amount) {
        return new ItemStack(this, amount, ids.get(e));
    }

    public ItemStack getStackFromString(String name) {
        return getStackFromData(property.parseValue(name).get());
    }

    public IBlockState getStateFromString(String name) {
        return getStateFromData(property.parseValue(name).get());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ids.get(state.getValue(property));
    }

    public int getMetaFromString(String name) {
        return getMetaFromState(getStateFromString(name));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.isFullBlock();
    }

    public ItemStack getStackFromString(String name, int size) {
        return getStackFromData(property.parseValue(name).get(), size);
    }

    public AbstractCustomData[] all() {
        return data;
    }

    @Nonnull
    protected ItemStack getCreativeStack(AbstractCustomData object) {
        return getStackFromData(object, 1);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockMultiCustom<>(getRegistryName(), this, defaults, data);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (AbstractCustomBlockData e : data) {
            ItemStack stack = getCreativeStack(e);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    protected void registerModel(Item item, AbstractCustomBlockData cd) {
        ModelLoader.setCustomModelResourceLocation(item, ids.get(cd),
                cd.getModel(new ResourceLocation(getRegistryName().getNamespace(), propertyName + "_" + cd.name), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        for (AbstractCustomBlockData e : data) {
            registerModel(item, e);
        }
    }

    public String getStringFromState(IBlockState state) {
        return property.getName(getDataFromState(state));
    }
}

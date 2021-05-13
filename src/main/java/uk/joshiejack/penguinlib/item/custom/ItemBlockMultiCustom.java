package uk.joshiejack.penguinlib.item.custom;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItemMulti;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemBlockMultiCustom <B, A extends AbstractCustomData.ItemOrBlock<B, A>> extends ItemBlock implements IPenguinItem, ICustomItemMulti {
    private final Object2IntMap<A> ids = new Object2IntOpenHashMap<>();
    private final Map<String, A> byName = Maps.newHashMap();
    private final A defaults;
    private final A[] data;
    private final String prefix;
    private final IPenguinBlock penguin;

    public ItemBlockMultiCustom(ResourceLocation registry, IPenguinBlock block, A defaults, A... data) {
        super((Block) block);
        this.penguin = block;
        this.defaults = defaults;
        this.data = data;
        for (int i = 0; i < this.data.length; i++) {
            ids.put(this.data[i], i);
            byName.put(this.data[i].name, this.data[i]);
        }

        prefix = registry.getNamespace() + ".block." + registry.getPath() + ".";
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
    }

    @Override
    public void init() {
        for (A cd: data) {
            cd.init(getStackFromData(cd));
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

    public A getDataFromStack(ItemStack stack) {
        return getDataFromMeta(stack.getItemDamage());
    }

    private A getDataFromMeta(int meta) {
        return ArrayHelper.getArrayValue(data, meta);
    }

    @Nonnull
    public ItemStack getStackFromData(A e, int size) {
        return new ItemStack(this, size, ids.getInt(e));
    }

    public ItemStack getStackFromData(A e) {
        return getStackFromData(e, 1);
    }

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
    public int getEntityLifespan(ItemStack stack, World world) {
        int thisLifespan = getDataFromStack(stack).lifespan;
        return thisLifespan == -1 ? defaults.lifespan == -1 ? 6000 : defaults.lifespan : thisLifespan;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            block.getSubBlocks(tab, items);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        penguin.registerModels(this);
    }
}

package uk.joshiejack.penguinlib.item.custom;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItemMulti;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemMultiCustom <A extends AbstractCustomData.ItemOrBlock> extends Item implements IPenguinItem, ICustomItemMulti {
    private final Object2IntMap<A> ids = new Object2IntOpenHashMap<>();
    private final Map<String, A> byName = Maps.newHashMap();
    private final A defaults;
    private final A[] data;
    private final String prefix;

    public ItemMultiCustom(ResourceLocation registry, A defaults, A... data) {
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
        return new ItemStack(this, size, ids.getOrDefault(e, 0));
    }

    public ItemStack getStackFromData(A e) {
        return getStackFromData(e, 1);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        A cd = getDataFromStack(player.getHeldItem(hand));
        if (cd.getScript() != null) {
            EnumActionResult result = Scripting.getResult(cd.getScript(), "onRightClick", EnumActionResult.PASS, player, hand);
            return new ActionResult<>(result, player.getHeldItem(hand));
        } else return super.onItemRightClick(world, player, hand);
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

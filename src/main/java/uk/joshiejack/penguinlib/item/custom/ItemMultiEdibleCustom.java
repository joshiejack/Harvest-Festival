package uk.joshiejack.penguinlib.item.custom;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import joptsimple.internal.Strings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.data.custom.item.ICustomItemMulti;
import uk.joshiejack.penguinlib.data.custom.item.CustomItemMultiEdibleData;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemMultiEdibleCustom extends ItemFood implements IPenguinItem, ICustomItemMulti {
    private final Object2IntMap<AbstractCustomData> ids = new Object2IntOpenHashMap<>();
    private final Map<String, AbstractCustomData> byName = Maps.newHashMap();
    private final CustomItemMultiEdibleData defaults;
    private final CustomItemMultiEdibleData[] data;
    private final String prefix;

    public ItemMultiEdibleCustom(ResourceLocation registry, CustomItemMultiEdibleData defaults, CustomItemMultiEdibleData... data) {
        super(0, 0.0F, false);
        this.defaults = defaults;
        this.data = data;
        for (int i = 0; i < this.data.length; i++) {
            ids.put(this.data[i], i);
            byName.put(this.data[i].name, this.data[i]);
        }

        prefix = registry.getNamespace() + ".item." + registry.getPath() + ".";
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(registry, this);
        if (defaults.alwaysEdible) setAlwaysEdible();
    }

    @Override
    public void init() {
        for (CustomItemMultiEdibleData cd: data) {
            cd.init(getStackFromData(cd)); //Init the data, and then... register synonymns
            StackHelper.registerSynonym(getRegistryName() + "#" + cd.name, getCreativeStack(cd));
        }
    }

    @Override
    public AbstractCustomData.ItemOrBlock getDefaults() {
        return defaults;
    }

    @Override
    public AbstractCustomData.ItemOrBlock[] getStates() {
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
    public CustomItemMultiEdibleData getDataFromStack(ItemStack stack) {
        return getDataFromMeta(stack.getItemDamage());
    }

    private CustomItemMultiEdibleData getDataFromMeta(int meta) {
        return ArrayHelper.getArrayValue(data, meta);
    }

    @Nonnull
    public ItemStack getStackFromData(AbstractCustomData e, int size) {
        return new ItemStack(this, size, ids.getInt(e));
    }

    public ItemStack getStackFromData(AbstractCustomData e) {
        return getStackFromData(e, 1);
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        return cd.hunger == -1 ? defaults.hunger == -1 ? 1 : defaults.hunger : cd.hunger;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        return cd.saturation == -1 ? defaults.saturation == -1 ? 0.6F : defaults.saturation : cd.saturation;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        return cd.consumeTime == -1 ? defaults.consumeTime == -1 ? 32 : defaults.consumeTime : cd.consumeTime;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        return cd.action == null ? defaults.action == null ? EnumAction.EAT : defaults.action : cd.action;
    }

    @Nonnull
    protected ItemStack getLeftovers(ItemStack stack) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        return Strings.isNullOrEmpty(cd.leftovers) ? defaults.getLeftovers() : cd.getLeftovers();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        CustomItemMultiEdibleData cd = getDataFromStack(stack);
        if (cd.getScript() != null) {
            Scripting.callFunction(cd.getScript(), "onFoodEaten", player, stack);
        } else {
            ItemStack leftovers = getLeftovers(stack);
            if (leftovers.isEmpty() && stack.getCount() > 0) {
                player.addItemStackToInventory(leftovers);
            }
        }
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
    protected ItemStack getCreativeStack(AbstractCustomData string) {
        return getStackFromData(string, 1);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (CustomItemMultiEdibleData string : data) {
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
        for (CustomItemMultiEdibleData cd : data) {
            ModelLoader.setCustomModelResourceLocation(this, ids.get(cd), cd.getModel(getRegistryName(), cd.name));
        }
    }
}

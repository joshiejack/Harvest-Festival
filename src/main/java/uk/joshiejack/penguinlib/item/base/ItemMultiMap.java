package uk.joshiejack.penguinlib.item.base;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItemMap;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class ItemMultiMap<E extends IPenguinItemMap<E>> extends Item implements IPenguinItem {
    private final Map<ResourceLocation, E> map;
    private final String name;

    public ItemMultiMap(ResourceLocation resource, Map<ResourceLocation, E> registry) {
        map = registry;
        name = resource.getNamespace() + ".item." + resource.getPath();
        setHasSubtypes(true); //metadata
        RegistryHelper.setRegistryAndLocalizedName(resource, this);
    }

    protected abstract E getNullEntry();

    @SuppressWarnings("ConstantConditions")
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

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName();
    }

    @Nonnull
    protected ItemStack getCreativeStack(E object) {
        return getStackFromResource(object.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        map.values().stream().filter(f -> f.getItemModelLocation() != null).forEach(v -> ModelBakery.registerItemVariants(this, v.getItemModelLocation()));
        ModelLoader.setCustomMeshDefinition(this, stack -> getObjectFromStack(stack).getItemModelLocation());
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
}

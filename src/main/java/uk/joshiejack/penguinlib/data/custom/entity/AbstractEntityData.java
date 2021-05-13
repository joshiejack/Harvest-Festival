package uk.joshiejack.penguinlib.data.custom.entity;

import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import uk.joshiejack.penguinlib.data.custom.HasLoot;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItemMap;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public abstract class AbstractEntityData<T extends Entity> extends AbstractCustomData<Integer, AbstractEntityData> implements HasLoot, IPenguinItemMap<AbstractEntityData<T>> {
    public static final Map<ResourceLocation, AbstractEntityData<Entity>> DATA_MAP = Maps.newHashMap();
    public static final AbstractEntityData EMPTY = new CustomSlimeData();
    public transient Class<? extends Entity> entity;
    public transient IRenderFactory<? super T> renderFactory;
    public ResourceLocation registryName;
    public ResourceLocation texture;
    public ResourceLocation lootTable;
    public String item;

    @Override
    public ResourceLocation getLootTable() {
        return lootTable;
    }

    @SuppressWarnings("unchecked")
    public int register(ResourceLocation registryName, Class<? extends Entity> clazz, int id) {
        DATA_MAP.put(registryName, (AbstractEntityData<Entity>) this);
        CustomLoader.subids.get(clazz).add(registryName);
        this.registryName = registryName;
        this.entity = clazz;
        return id;
    }

    @SideOnly(Side.CLIENT)
    public void setRenderer(IRenderFactory<? super T> renderFactory) {
        this.renderFactory = renderFactory;
    }

    public ItemStack getDropItem(int size) {
        if (Strings.isNullOrEmpty(item)) return ItemStack.EMPTY;
        ItemStack stack = StackHelper.getStackFromString(item);
        stack.setCount(size);
        return stack;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public String getLocalizedName() {
        return registryName.getPath();
    }

    @Override
    public ModelResourceLocation getItemModelLocation() {
        return new ModelResourceLocation(registryName.getNamespace() + ":entity#" + registryName.getPath());
    }
}

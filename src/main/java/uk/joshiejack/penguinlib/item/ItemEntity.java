package uk.joshiejack.penguinlib.item;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.client.renderer.item.EntitySpawnerRenderer;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class ItemEntity extends ItemSingular {
    public static final Map<ResourceLocation, Entity> entities = Maps.newHashMap();
    private static final ResourceLocation PIG = new ResourceLocation("minecraft", "pig");

    public ItemEntity() {
        super(new ResourceLocation(PenguinLib.MOD_ID, "entity"));
        setCreativeTab(PenguinLib.CUSTOM_TAB);
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound()) return super.getItemStackDisplayName(stack);
        String s = ("" + I18n.translateToLocal(this.getTranslationKey() + ".name")).trim();
        String s1 = EntityList.getTranslationName(new ResourceLocation(stack.getTagCompound().getString("Entity")));

        if (s1 != null) {
            s = I18n.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    public ItemStack withEntity(String entity) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Entity", entity);
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (EntityEntry entity : GameData.getEntityRegistry()) {
                if (EntityLivingBase.class.isAssignableFrom(entity.getEntityClass())) {
                    items.add(withEntity(entity.getRegistryName().toString()));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
        PenguinItems.ENTITY.setTileEntityItemStackRenderer(new EntitySpawnerRenderer());
    }

    @SideOnly(Side.CLIENT)
    public static Entity fromResource(ResourceLocation resource) {
        if (!entities.containsKey(resource)) {
            entities.put(resource, EntityList.createEntityByIDFromName(resource, Minecraft.getMinecraft().world));
        }

        return entities.get(resource);
    }

    @SideOnly(Side.CLIENT)
    public Entity getEntityFromStack(ItemStack stack) {
        return fromResource(!stack.hasTagCompound() ? PIG : new ResourceLocation(stack.getTagCompound().getString("Entity")));
    }
}

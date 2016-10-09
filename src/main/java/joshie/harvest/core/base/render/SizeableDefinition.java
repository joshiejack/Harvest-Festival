package joshie.harvest.core.base.render;

import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.core.util.Sizeable;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;


public class SizeableDefinition implements ItemMeshDefinition {
    public static final SizeableDefinition INSTANCE = new SizeableDefinition();
    protected final HashMap<Sizeable, EnumMap<Size, ModelResourceLocation>> models = new HashMap<>();

    private SizeableDefinition() {
        ModelBakery.registerItemVariants(HFCore.SIZEABLE);
    }

    public void registerEverything() {
        for (Sizeable e : SizeableRegistry.REGISTRY) {
            EnumMap<Size, ModelResourceLocation> map = new EnumMap<>(Size.class);
            for (Size size: Size.values()) {
                ModelResourceLocation model = new ModelResourceLocation(new ResourceLocation(e.getRegistryName().getResourceDomain(), e.getRegistryName().getResourcePath()), size.name().toLowerCase(Locale.ENGLISH));
                ModelBakery.registerItemVariants(HFCore.SIZEABLE, model);
                map.put(size, model);
            }

            models.put(e, map);
        }
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        Sizeable sizeable = HFCore.SIZEABLE.getObjectFromStack(stack);
        Size size = HFCore.SIZEABLE.getSize(stack);
        return models.get(sizeable).get(size);
    }
}

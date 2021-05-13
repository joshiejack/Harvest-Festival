package uk.joshiejack.penguinlib.util;

import uk.joshiejack.penguinlib.item.interfaces.IPenguinItemMap;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class PenguinRegistry {
    public final ResourceLocation resource;

    @SuppressWarnings("unchecked")
    public <T extends PenguinRegistry> PenguinRegistry(Map<ResourceLocation, T> registry, ResourceLocation resource) {
        this.resource = resource;
        registry.put(resource, (T) this);
    }

    /** Return the id of this item **/
    public ResourceLocation getRegistryName() {
        return resource;
    }

    public static class Item<E> extends PenguinRegistry implements IPenguinItemMap<E> {
        protected final String unlocalizedKey;
        protected final String unlocalizedName;
        protected final ModelResourceLocation itemModel;

        public <T extends PenguinRegistry> Item(String type, Map<ResourceLocation, T> registry, ResourceLocation resource) {
            super(registry, resource);
            this.unlocalizedKey = resource.getNamespace() + "." + type + "." + resource.getPath();
            this.unlocalizedName = unlocalizedKey + ".name";
            this.itemModel = new ModelResourceLocation(resource.getNamespace() + ":" + type + "#" + resource.getPath());
        }

        @Override
        public String getLocalizedName() {
            return StringHelper.localize(unlocalizedName);
        }

        @Override
        public ModelResourceLocation getItemModelLocation() {
            return itemModel;
        }
    }
}

package uk.joshiejack.harvestcore.client.renderer.block;

import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.registry.IRegistry;

import java.util.Locale;
import java.util.Objects;

public class QualityModelReplaceHandler<I extends Item> {
    public static final QualityModelReplaceHandler<Item> BASIC = new QualityModelReplaceHandler();
    public static final QualityModelReplaceHandler<?> PENGUIN = new QualityModelReplacePenguin();

    @SuppressWarnings("all")
    public  void replace(I item, IRegistry<ModelResourceLocation, IBakedModel> registry) {
        ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
        registry.putObject(resource, new QualityModel(registry.getObject(resource)));
    }

    public static class QualityModelReplacePenguin extends QualityModelReplaceHandler<ItemMultiEdible<?>> {
        @SuppressWarnings("all")
        @Override
        public void replace(ItemMultiEdible<?> item, IRegistry<ModelResourceLocation, IBakedModel> registry) {
            for (Enum e: item.getValues()) {
                ModelResourceLocation resource = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), e.name().toLowerCase(Locale.ENGLISH));
                registry.putObject(resource, new QualityModel(registry.getObject(resource)));
            }
        }
    }

    public static class QualityModelReplaceEnum<E extends Enum<?>> extends QualityModelReplaceHandler<Item> {
        private final Class<E> enumClass;

        private QualityModelReplaceEnum(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        public static <E extends Enum<?>> QualityModelReplaceEnum of(Class<E> enumClass) {
            return new QualityModelReplaceEnum<>(enumClass);
        }

        @Override
        public void replace(Item item, IRegistry<ModelResourceLocation, IBakedModel> registry) {
            for (Enum<?> e: enumClass.getEnumConstants()) {
                ModelResourceLocation resource = new ModelResourceLocation(e.name().toLowerCase(Locale.ENGLISH), "inventory");
                registry.putObject(resource, new QualityModel(registry.getObject(resource)));
            }
        }
    }

    public static class QualityModelReplaceNamed extends QualityModelReplaceHandler<Item> {
        private final String[] names;

        public QualityModelReplaceNamed(String[] names) {
            this.names = names;
        }

        @Override
        public void replace(Item item, IRegistry<ModelResourceLocation, IBakedModel> registry) {
            for (String name: names) {
                ModelResourceLocation resource = new ModelResourceLocation(name, "inventory");
                registry.putObject(resource, new QualityModel(registry.getObject(resource)));
            }
        }
    }
}

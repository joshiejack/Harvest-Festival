package uk.joshiejack.harvestcore.client.renderer.block;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.penguinlib.client.util.ModelCache;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

@SideOnly(Side.CLIENT)
public class QualityItemList extends ItemOverrideList {
    private final Cache<Quality, QualityModel> cache = CacheBuilder.newBuilder().maximumSize(256).build();

    public QualityItemList() {
        super(ImmutableList.of());
    }

    @SuppressWarnings("all")
    @Nullable
    private Quality getQualityFromStack(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Quality")) {
            return Quality.REGISTRY.get(new ResourceLocation(stack.getTagCompound().getString("Quality")));
        }

        return null;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityy) {
        Quality quality = getQualityFromStack(stack);
        if (quality == null || quality.modifier() == 1D) {
            return originalModel;
        }

        cache.invalidateAll();
        QualityModel model = (QualityModel) originalModel;
        try {
            return cache.get(quality, () -> stack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN) ?
                    new QualityModel(model, QualityModelOverride.PUMPKINS.get(quality)):
                    new QualityModel(model, ModelCache.INSTANCE.getOrLoadBakedModel(quality.model())));
        } catch (ExecutionException ex) {
            return originalModel;
        }
    }

}

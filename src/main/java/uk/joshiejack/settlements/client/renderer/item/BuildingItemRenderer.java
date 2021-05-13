package uk.joshiejack.settlements.client.renderer.item;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingWorldAccess;
import uk.joshiejack.settlements.item.AdventureItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class BuildingItemRenderer extends TileEntityItemStackRenderer implements ISelectiveResourceReloadListener {
    private static final Cache<Pair<Building, String>, BuildingRenderer> renderers = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).build();

    private BuildingRenderer getRenderer(Building building, String name, int minFloor) throws ExecutionException {
        return renderers.get(Pair.of(building, name), () -> new BuildingRenderer(new BuildingWorldAccess(building, Rotation.NONE), minFloor));
    }

    public BuildingItemRenderer() {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
    }

    private static class RenderInfo {
        public Building building;
        float scale, offsetX, offsetY, offsetZ;
        int floorStart;

        private RenderInfo() {
            building = Building.NULL;
            scale = 0.068F;
            offsetX = 0.8F;
            offsetY = 0.6F;
            offsetZ = 0.5F;
            floorStart = 1;
        }

        public static RenderInfo fromString(ItemStack stack) {
            RenderInfo info = new RenderInfo();
            info.building = AdventureItems.previewer.getObjectFromStack(stack);
            String[] vars = stack.getDisplayName().split(";");
            if (vars.length == 5) {
                info.scale = Float.parseFloat(vars[0]);
                info.offsetX = Float.parseFloat(vars[1]);
                info.offsetY = Float.parseFloat(vars[2]);
                info.offsetZ = Float.parseFloat(vars[3]);
                info.floorStart = Integer.parseInt(vars[4]);
            }

            return info;
        }
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        try {
            RenderInfo info = RenderInfo.fromString(stack);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.color(1F, 1F, 1F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(info.offsetX, info.offsetY, info.offsetZ);
            float scale = info.scale;
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GlStateManager.translate(0.0F, -1.501F, 0.0F);
            getRenderer(info.building, stack.getDisplayName(), info.floorStart).draw();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        } catch (ExecutionException ex) { /**/ ex.printStackTrace(); }
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.MODELS) || resourcePredicate.test(VanillaResourceType.TEXTURES)) {
            renderers.invalidateAll();
        }
    }
}

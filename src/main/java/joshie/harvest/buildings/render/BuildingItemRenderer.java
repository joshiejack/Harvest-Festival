package joshie.harvest.buildings.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.render.BuildingItemRenderer.BuildingTile;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

public class BuildingItemRenderer extends TileEntitySpecialRenderer<BuildingTile> {
    private final BuildingVertexUploader vertexUploader = new BuildingVertexUploader();
    private Cache<Building, BuildingRenderer> cache = CacheBuilder.newBuilder().build();
    private BuildingRenderer getRenderer(Building building) throws ExecutionException {
        return cache.get(building, () -> building == HFBuildings.FESTIVAL_GROUNDS ?
                  new BuildingRenderer(new BuildingAccess(building, Rotation.NONE), new BuildingKey(Rotation.NONE, building)):
                  new BuildingRendererNoFloor(new BuildingAccess(building, Rotation.NONE), new BuildingKey(Rotation.NONE, building)));
    }

    @Override
    public void render(@Nullable BuildingTile fake, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        try {
            if (fake != null) {
                Building building = HFBuildings.TOWNHALL;
                RenderHelper.disableStandardItemLighting();
                GlStateManager.color(1F, 1F, 1F);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.8F, 0.6F, 0.5F);
                float scale = 0.068F;
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                GlStateManager.translate(0.0F, -1.501F, 0.0F);
                getRenderer(building).draw(vertexUploader);
                GlStateManager.disableRescaleNormal();
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
            }
        } catch (ExecutionException ex) { /**/}
    }

    public static class BuildingTile extends EntityItemRenderer {
        public static final BuildingTile INSTANCE = new BuildingTile();
    }
}

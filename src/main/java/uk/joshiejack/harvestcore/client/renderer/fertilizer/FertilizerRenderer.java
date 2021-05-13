package uk.joshiejack.harvestcore.client.renderer.fertilizer;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.client.renderer.block.BakedFertilizer;
import uk.joshiejack.penguinlib.client.util.ModelCache;
import uk.joshiejack.penguinlib.template.render.TemplateRendererer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class FertilizerRenderer extends TemplateRendererer<FertilizerWorldAccess> {
    private static final ResourceLocation FERTILIZER_MODEL = new ResourceLocation(HarvestCore.MODID, "block/fertilizer");
    public FertilizerRenderer(FertilizerWorldAccess access, BlockPos pos) {
        super(false, access, pos);
    }

    @Override
    protected void setupRender(@Nonnull FertilizerWorldAccess access) {
        IBakedModel original = ModelCache.INSTANCE.getOrLoadBakedModel(FERTILIZER_MODEL);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(access.fertilizer.getBlockSprite().toString());
        BakedFertilizer model = new BakedFertilizer(original, sprite);
        BufferBuilder buffer = renderer.getWorldRendererByLayer(BlockRenderLayer.CUTOUT);
        buffer.begin(7, DefaultVertexFormats.BLOCK);
        for (BlockPos pos: access.getBlockMap().keySet()) {
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
                    .renderModel(Minecraft.getMinecraft().world, model, Blocks.FARMLAND.getDefaultState(), pos, buffer, true);
        }
    }
}

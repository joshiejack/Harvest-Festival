package uk.joshiejack.harvestcore.client.renderer.block;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedPenguin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.List;

public class BakedFertilizer extends BakedPenguin {
    private final List<BakedQuad> quads = Lists.newArrayList();

    public BakedFertilizer(IBakedModel parent, TextureAtlasSprite sprite) {
        super(parent);
        super.getQuads(null, null, 0L).forEach((q) -> quads.add(new BakedQuadRetextured(q, sprite)));
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        return quads;
    }
}

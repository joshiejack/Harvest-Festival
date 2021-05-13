package uk.joshiejack.horticulture.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import uk.joshiejack.horticulture.block.BlockStump;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedPenguin;
import uk.joshiejack.penguinlib.util.PropertyBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class BakedStump extends BakedPenguin {
    private final Int2ObjectMap<Map<IBlockState, Map<EnumFacing, List<BakedQuad>>>> models = new Int2ObjectOpenHashMap<>();
    private final IBakedModel[] mushroom_models;

    public BakedStump(IBakedModel parent, IBakedModel[] mushroom_models) {
        super(parent);
        this.mushroom_models = mushroom_models;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extended = ((IExtendedBlockState)state);
            IBlockState mushroom = extended.getValue(PropertyBlockState.INSTANCE);
            int stage = extended.getValue(BlockStump.stage);
            if (!models.containsKey(stage)) {
                models.put(stage, Maps.newHashMap());
            }

            Map<IBlockState, Map<EnumFacing, List<BakedQuad>>> models = this.models.get(stage);
            if (!models.containsKey(mushroom)) {
                models.put(mushroom, Maps.newHashMap());
            }

            Map<EnumFacing, List<BakedQuad>> map = models.get(mushroom);
            if (!map.containsKey(side)) {
                List<BakedQuad> quads = Lists.newArrayList(super.getQuads(state, side, rand));
                TextureAtlasSprite texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(mushroom).getParticleTexture();
                if (stage > 0) {
                    mushroom_models[stage - 1].getQuads(state, side, rand).forEach(quad -> quads.add(new BakedQuadRetextured(quad, texture)));
                }

                map.put(side, ImmutableList.copyOf(quads));
            }

            //Yes my love
            return map.get(side);
        } else return super.getQuads(state, side, rand);
    }

}

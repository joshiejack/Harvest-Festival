package uk.joshiejack.penguinlib.client.renderer.block.model;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.client.util.WeightedFloorOverlay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BakedFloor extends BakedPenguin {
    private final IBakedModel overlay;
    protected final List<WeightedFloorOverlay> textures;
    private final Map<TextureAtlasSprite, Map<IBlockState, EnumMap<EnumFacing, List<BakedQuad>>>> cache = Maps.newHashMap();
    private final int totalWeight;

    public BakedFloor(IBakedModel parent, IBakedModel overlay, List<WeightedFloorOverlay> overlays) {
        super(parent);
        this.overlay = overlay;
        this.textures = overlays;
        this.totalWeight = WeightedRandom.getTotalWeight(textures);
    }

    private TextureAtlasSprite getRandomTexture(long rand) {
        return (WeightedRandom.getRandomItem(textures, Math.abs((int) rand >> 16) % totalWeight)).sprite;
    }

    private <R, T, V> Map <T, V> getOrCreateMap(R key, Map<R, Map<T, V>> map) {
        if (!map.containsKey(key) || map.get(key) == null) {
            map.put(key, Maps.newHashMap());
        } return map.get(key);
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        if (state == null || side == null) return super.getQuads(null, side, rand);
        TextureAtlasSprite sprite = getRandomTexture(rand);
        Map<IBlockState, EnumMap<EnumFacing, List<BakedQuad>>> cache = getOrCreateMap(sprite, this.cache);
        if (!cache.containsKey(state)) {
            cache.put(state, new EnumMap<>(EnumFacing.class));
        }

        EnumMap<EnumFacing, List<BakedQuad>> map = cache.get(state);
        if (!map.containsKey(side)) {
            List<BakedQuad> quads = new ArrayList<>(super.getQuads(state, side, rand));
            overlay.getQuads(null, null, rand).forEach(quad -> quads.add(new BakedQuadRetextured(quad, sprite)));
            map.put(side, quads);
        }

        return map.get(side);
    }
}

package uk.joshiejack.horticulture.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedPenguin;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedTintedQuad;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class BakedLeaves extends BakedPenguin {
    private final Map<EnumFacing, List<BakedQuad>> quads_fancy = Maps.newHashMap();
    private final Map<EnumFacing, List<BakedQuad>> quads_simple = Maps.newHashMap();
    private final TextureAtlasSprite sprite;
    private final IBakedModel base;

    public BakedLeaves(IBakedModel parent, IBakedModel base, TextureAtlasSprite sprite) {
        super(parent);
        this.base = base;
        this.sprite = sprite;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        boolean fancy = Minecraft.getMinecraft().gameSettings.fancyGraphics;
        Map<EnumFacing, List<BakedQuad>> quads = fancy ? quads_fancy : quads_simple;
        if (!quads.containsKey(side)) {
            List<BakedQuad> list = Lists.newArrayList();
            if (fancy) list.addAll(base.getQuads(state, side, rand));
            else base.getQuads(state, side, rand).stream().map(quad -> new BakedQuadRetextured(quad, sprite)).forEachOrdered(list::add);
            BakedLeaves.super.getQuads(state, side, 0).stream().map(BakedTintedQuad:: new).forEachOrdered(list::add);
            quads.put(side, list);
            return list;
        } else return quads.get(side);
    }

}



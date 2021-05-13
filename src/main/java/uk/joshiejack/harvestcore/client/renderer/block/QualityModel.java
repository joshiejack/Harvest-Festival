package uk.joshiejack.harvestcore.client.renderer.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QualityModel implements IBakedModel {
    private ImmutableMap<TransformType, Pair<QualityModel, TRSRTransformation>> transforms;
    private final QualityItemList override;
    private final IBakedModel original;
    private TextureAtlasSprite sprite;
    private IBakedModel model;

    public QualityModel(IBakedModel original) {
        this.original = original;
        this.override = new QualityItemList();
        this.setupTransformations();
    }

    public QualityModel(IBakedModel original, QualityItemList override, IBakedModel model, TextureAtlasSprite sprite) {
        this.original = original;
        this.override = override;
        this.model = model;
        this.sprite = sprite;
    }

    public QualityModel(QualityModel model, TextureAtlasSprite sprite) {
        this.original = model.original;
        this.override = model.override;
        this.sprite = sprite;
        this.setupTransformations();
    }

    public QualityModel(QualityModel model, IBakedModel model2) {
        this.original = model.original;
        this.override = model.override;
        this.model = model2;
        //this.sprite = sprite;
        this.setupTransformations();
    }

    private void setupTransformations() {
        if (original != null) {
            ImmutableMap.Builder<TransformType, Pair<QualityModel, TRSRTransformation>> builder = ImmutableMap.builder();
            for (TransformType type : TransformType.values()) {
                Pair<? extends IBakedModel, Matrix4f> p = original.handlePerspective(type);
                builder.put(type, Pair.of(new QualityModel(p.getLeft(), override, model, sprite), new TRSRTransformation(p.getRight())));
            }

            transforms = builder.build();
        } else transforms = ImmutableMap.of();
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> list = original.getQuads(state, side, rand);
        if (sprite != null && list.size() > 0 && model == null) {
            List<BakedQuad> quads = new ArrayList<>();
            if (side == EnumFacing.EAST) {
                quads.addAll(list.stream().map(q -> new BakedQuadRetextured(q, sprite)).collect(Collectors.toList()));
            } else quads.addAll(list);

            return quads;
        } else if (model != null && list.size() > 0) {
            List<BakedQuad> quads = Lists.newArrayList(list);
            quads.addAll(model.getQuads(null, null, rand));
            return quads;
        } else return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return original.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return original.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return original.isBuiltInRenderer();
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return original.getParticleTexture();
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return original.getItemCameraTransforms();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return override;
    }

    @Nonnull
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull TransformType theType) {
        if (transforms == null || transforms.isEmpty()) setupTransformations();
        Pair<QualityModel, TRSRTransformation> p = transforms.get(theType);
        return Pair.of(p.getLeft(), p.getRight().getMatrix());
    }
}

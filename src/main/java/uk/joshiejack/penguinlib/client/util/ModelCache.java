package uk.joshiejack.penguinlib.client.util;

import com.google.common.base.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public enum ModelCache implements ISelectiveResourceReloadListener {
    INSTANCE;

    public static final IModelState DEFAULTMODELSTATE = new IModelState() {
        @Override
        public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
            return Optional.empty();
        }
    };
    public static final VertexFormat DEFAULTVERTEXFORMAT = DefaultVertexFormats.BLOCK;
    public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULTTEXTUREGETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
        @Override
        public TextureAtlasSprite apply(ResourceLocation texture) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
            //return Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(texture.toString());
        }
    };

    private final Map<ResourceLocation, IModel> cache = new HashMap<ResourceLocation, IModel>();
    private final Map<ResourceLocation, IBakedModel> bakedCache = new HashMap<ResourceLocation, IBakedModel>();

    public IModel getOrLoadModel(ResourceLocation location)
    {
        IModel model = cache.get(location);
        if(model == null)
        {
            try
            {
                model = ModelLoaderRegistry.getModel(location);
            }
            catch(Exception e) {
                e.printStackTrace();
                model = ModelLoaderRegistry.getMissingModel();
            }
            cache.put(location, model);
        }
        return model;
    }

    public IBakedModel getModel(ResourceLocation key) {
        return bakedCache.get(key);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        IBakedModel model = getModel(key);
        if(model == null) {
            ModelRotation sprite = ModelRotation.X0_Y0;
            model = getOrLoadModel(location).bake(state, format, textureGetter);
            bakedCache.put(key, model);
        }
        return model;
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, IModelState state, VertexFormat format) {
        return getOrLoadModel(key, location, state, format, DEFAULTTEXTUREGETTER);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, IModelState state, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        return getOrLoadModel(key, location, state, DEFAULTVERTEXFORMAT, textureGetter);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        return getOrLoadModel(key, location, DEFAULTMODELSTATE, format, textureGetter);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, IModelState state) {
        return getOrLoadModel(key, location, state, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, VertexFormat format) {
        return getOrLoadModel(key, location, DEFAULTMODELSTATE, format, DEFAULTTEXTUREGETTER);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        return getOrLoadModel(key, location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, textureGetter);
    }

    public IBakedModel getOrLoadModel(ResourceLocation key, ResourceLocation location) {
        return getOrLoadModel(key, location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
    }

    public IBakedModel getOrLoadBakedModel(ResourceLocation location) {
        return getOrLoadModel(location, location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        cache.clear();
        bakedCache.clear();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        cache.clear();
        bakedCache.clear();
    }
}

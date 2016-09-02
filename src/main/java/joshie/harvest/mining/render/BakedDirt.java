package joshie.harvest.mining.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.core.render.BakedHF;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockDirt;
import joshie.harvest.mining.block.BlockDirt.TextureStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BakedDirt extends BakedHF {
    private final IBakedModel overlay;
    protected List<WeightedTexture> textures;
    private int totalWeight;

    public BakedDirt(IBakedModel parent, IBakedModel overlay, List<WeightedTexture> overlays) {
        super(parent);
        this.overlay = overlay;
        this.textures = overlays;
        this.totalWeight = WeightedRandom.getTotalWeight(textures);
    }

    private TextureAtlasSprite getRandomTexture(long rand) {
        return (WeightedRandom.getRandomItem(textures, Math.abs((int)rand >> 16) % totalWeight)).sprite;
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        List<BakedQuad> quads = new ArrayList<>(BakedDirt.super.getQuads(state, side, rand));
        overlay.getQuads(state, side, rand).stream().map(quad -> new BakedQuadRetextured(quad, getRandomTexture(rand))).forEachOrdered(quads::add);
        return quads;
    }

    @HFEvents
    public static class StateMapper extends DefaultStateMapper {
        private static final String[] VALID_STATES = new String[] {
                "ne=blank,nw=blank,se=blank,sw=blank",
                "ne=blank,nw=blank,se=blank,sw=inner",
                "ne=blank,nw=blank,se=horizontal,sw=horizontal",
                "ne=blank,nw=blank,se=inner,sw=blank",
                "ne=blank,nw=blank,se=inner,sw=inner",
                "ne=blank,nw=inner,se=blank,sw=blank",
                "ne=blank,nw=inner,se=blank,sw=inner",
                "ne=blank,nw=inner,se=horizontal,sw=horizontal",
                "ne=blank,nw=inner,se=inner,sw=blank",
                "ne=blank,nw=inner,se=inner,sw=inner",
                "ne=blank,nw=vertical,se=blank,sw=vertical",
                "ne=blank,nw=vertical,se=horizontal,sw=outer",
                "ne=blank,nw=vertical,se=inner,sw=vertical",
                "ne=horizontal,nw=horizontal,se=blank,sw=blank",
                "ne=horizontal,nw=horizontal,se=blank,sw=inner",
                "ne=horizontal,nw=horizontal,se=horizontal,sw=horizontal",
                "ne=horizontal,nw=horizontal,se=inner,sw=blank",
                "ne=horizontal,nw=horizontal,se=inner,sw=inner",
                "ne=horizontal,nw=outer,se=blank,sw=vertical",
                "ne=horizontal,nw=outer,se=horizontal,sw=outer",
                "ne=horizontal,nw=outer,se=inner,sw=vertical",
                "ne=inner,nw=blank,se=blank,sw=blank",
                "ne=inner,nw=blank,se=blank,sw=inner",
                "ne=inner,nw=blank,se=horizontal,sw=horizontal",
                "ne=inner,nw=blank,se=inner,sw=blank",
                "ne=inner,nw=blank,se=inner,sw=inner",
                "ne=inner,nw=inner,se=blank,sw=blank",
                "ne=inner,nw=inner,se=blank,sw=inner",
                "ne=inner,nw=inner,se=horizontal,sw=horizontal",
                "ne=inner,nw=inner,se=inner,sw=blank",
                "ne=inner,nw=inner,se=inner,sw=inner",
                "ne=inner,nw=vertical,se=blank,sw=vertical",
                "ne=inner,nw=vertical,se=horizontal,sw=outer",
                "ne=inner,nw=vertical,se=inner,sw=vertical",
                "ne=outer,nw=horizontal,se=outer,sw=horizontal",
                "ne=outer,nw=horizontal,se=vertical,sw=blank",
                "ne=outer,nw=horizontal,se=vertical,sw=inner",
                "ne=outer,nw=outer,se=outer,sw=outer",
                "ne=outer,nw=outer,se=vertical,sw=vertical",
                "ne=vertical,nw=blank,se=outer,sw=horizontal",
                "ne=vertical,nw=blank,se=vertical,sw=blank",
                "ne=vertical,nw=blank,se=vertical,sw=inner",
                "ne=vertical,nw=inner,se=outer,sw=horizontal",
                "ne=vertical,nw=inner,se=vertical,sw=blank",
                "ne=vertical,nw=inner,se=vertical,sw=inner",
                "ne=vertical,nw=vertical,se=outer,sw=outer",
                "ne=vertical,nw=vertical,se=vertical,sw=vertical"
        };

        private boolean isValidState(String state) {
            for (String s: VALID_STATES) {
                if (state.equals(s)) return true;
            }

            return false;
        }

        @Override
        public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block) {
            if (block == HFMining.DIRT) {
                mapStateModelLocations.put(new BlockStateContainer(block).getBaseState(), new ModelResourceLocation(block.getRegistryName(), "overlay"));
                for (IBlockState iblockstate : block.getBlockState().getValidStates()) {
                    ModelResourceLocation model = getModelResourceLocation(iblockstate);
                    if (model != null) {
                        mapStateModelLocations.put(iblockstate, model);
                    }
                }
            }

            return mapStateModelLocations;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            String properties = this.getPropertyString(state.getProperties());
            if (!isValidState(properties)) return null;
            return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), properties);
        }

        @SubscribeEvent
        public void onStitch(TextureStitchEvent event) {
            for (TextureType type: TextureType.values()) {
                event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/mine/overlays/" + type.name().toLowerCase(Locale.US)));
            }
        }

        @SubscribeEvent
        public void onBaking(ModelBakeEvent event) {
            //Load in the overlay textures
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            IBakedModel overlay = registry.getObject(new ModelResourceLocation(new ResourceLocation(MODID, "dirt"), "overlay"));
            List<WeightedTexture> list = new ArrayList<>();
            for (TextureType type: TextureType.values()) {
                TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("harvestfestival:blocks/mine/overlays/" + type.name().toLowerCase(Locale.US));
                list.add(new WeightedTexture(sprite, type.weight));
            }

            ResourceLocation[] resourceLocations = new ResourceLocation[] { new ResourceLocation(MODID, "dirt"), new ResourceLocation(MODID, "dirt_decorative")};
            Cache<IBakedModel, BakedDirt> cache = CacheBuilder.newBuilder().build();
            ResourceLocation dirt = new ResourceLocation(MODID, "dirt");
            for (ResourceLocation resource: resourceLocations) {
                //Change the models
                for (BlockDirt.TextureStyle ne : TextureStyle.values()) {
                    for (BlockDirt.TextureStyle nw : TextureStyle.values()) {
                        for (BlockDirt.TextureStyle se : TextureStyle.values()) {
                            for (BlockDirt.TextureStyle sw : TextureStyle.values()) {
                                String state = String.format("ne=%s,nw=%s,se=%s,sw=%s", ne.getName(), nw.getName(), se.getName(), sw.getName());
                                if (isValidState(state)) {
                                    ModelResourceLocation location = getModelLocation(resource, state); //Grab the location of this model
                                    IBakedModel original = registry.getObject(getModelLocation(dirt, state)); //Grab the normal dirt state
                                    try {
                                        IBakedModel clone = cache.get(original, new Callable<BakedDirt>() {
                                            @Override
                                            public BakedDirt call() throws Exception {
                                                return new BakedDirt(original, overlay, list);
                                            }
                                        });

                                        registry.putObject(location, clone);
                                    } catch (Exception e) {}
                                }
                            }
                        }
                    }
                }
            }
        }

        private ModelResourceLocation getModelLocation(ResourceLocation resource, String state) {
            return new ModelResourceLocation(resource, state);
        }
    }

    public enum TextureType {
        BLANK(150), BONES1(1), BONES2(1), BONES3(1), BONES4(1), GRASS1(7), LEAVES1(4), LEAVES2(4), LEAVES3(1), LEAVES4(1),
        PEBBLE1(4), PEBBLE2(4), PEBBLE3(3), PEBBLE4(1), PEBBLE5(2), PEBBLE6(2), ROCK1(4), ROCK2(4);

        private final int weight;

        TextureType(int weight) {
            this.weight = weight;
        }
    }

    private static class WeightedTexture extends WeightedRandom.Item {
        public TextureAtlasSprite sprite;

        public WeightedTexture(TextureAtlasSprite sprite, int weight) {
            super(weight);
            this.sprite = sprite;
        }
    }
}


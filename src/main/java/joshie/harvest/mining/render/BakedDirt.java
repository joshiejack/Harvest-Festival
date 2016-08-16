package joshie.harvest.mining.render;

import joshie.harvest.core.render.BakedHF;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.mining.blocks.BlockDirt;
import joshie.harvest.mining.blocks.BlockDirt.TextureStyle;
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
import java.util.Map;

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
        TextureAtlasSprite sprite = getRandomTexture(rand);
        if (sprite != null) overlay.getQuads(state, side, rand).stream().map(quad -> new BakedQuadRetextured(quad, sprite)).forEachOrdered(quads::add);
        return quads;
    }

    @HFEvents
    public static class StateMapper extends DefaultStateMapper {
        @Override
        public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block) {
            mapStateModelLocations.put(new BlockStateContainer(block).getBaseState(), new ModelResourceLocation(block.getRegistryName(), "overlay"));
            for (IBlockState iblockstate : block.getBlockState().getValidStates()) {
                mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
            }

            return mapStateModelLocations;
        }

        @SubscribeEvent
        public void onStitch(TextureStitchEvent event) {
            for (TextureType type: TextureType.values()) {
                event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/mine/overlays/" + type.name().toLowerCase()));
            }
        }

        @SubscribeEvent
        public void onBaking(ModelBakeEvent event) {
            //Load in the overlay textures
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            IBakedModel overlay = registry.getObject(new ModelResourceLocation(new ResourceLocation(MODID, "dirt"), "overlay"));

            //Create the list of sprites
            List<WeightedTexture> list = new ArrayList<>();
            for (TextureType type: TextureType.values()) {
                if (type == TextureType.BLANK) list.add(new WeightedTexture(null, type.weight));
                else list.add(new WeightedTexture(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("harvestfestival:blocks/mine/overlays/" + type.name().toLowerCase()), type.weight));
            }

            //Change the models
            ResourceLocation[] resourceLocations = new ResourceLocation[] { new ResourceLocation(MODID, "dirt"), new ResourceLocation(MODID, "dirt_decorative")};
            for (ResourceLocation resource: resourceLocations) {
                for (BlockDirt.TextureStyle ne : TextureStyle.values()) {
                    for (BlockDirt.TextureStyle nw : TextureStyle.values()) {
                        for (BlockDirt.TextureStyle se : TextureStyle.values()) {
                            for (BlockDirt.TextureStyle sw : TextureStyle.values()) {
                                ModelResourceLocation location = getModelLocation(resource, ne, nw, se, sw);
                                IBakedModel original = registry.getObject(location);
                                registry.putObject(location, new BakedDirt(original, overlay, list));
                            }
                        }
                    }
                }
            }
        }

        private ModelResourceLocation getModelLocation(ResourceLocation resource, TextureStyle ne, TextureStyle nw, TextureStyle se, TextureStyle sw) {
            return new ModelResourceLocation(resource, String.format("ne=%s,nw=%s,se=%s,sw=%s", ne, nw, se, sw));
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


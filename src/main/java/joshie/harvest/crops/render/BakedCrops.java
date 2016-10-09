package joshie.harvest.crops.render;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.base.render.BakedHF;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BakedCrops extends BakedHF {
    public BakedCrops(IBakedModel parent) {
        super(parent);
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        List<BakedQuad> quads = new ArrayList<>();
        BakedCrops.super.getQuads(state, side, rand).stream().map(BakedTintedQuad :: new).forEachOrdered(quads::add);
        return quads;
    }

    @HFEvents
    public static class TintedMapper extends DefaultStateMapper {
        @SubscribeEvent
        public void onBaking(ModelBakeEvent event) {
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            for (Crop crop: Crop.REGISTRY) {
                if (crop != Crop.NULL_CROP) {
                    if (crop.skipLoadingRender()) {
                        for (IBlockState state: crop.getStateHandler().getValidStates()) {
                            IBakedModel original = registry.getObject(getModelResourceLocation(state));
                            registry.putObject(getModelResourceLocation(state), new BakedCrops(original));
                        }
                    }
                }
            }
        }
    }
}


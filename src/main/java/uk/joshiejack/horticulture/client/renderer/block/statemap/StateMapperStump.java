package uk.joshiejack.horticulture.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.horticulture.client.renderer.block.model.BakedStump;
import uk.joshiejack.horticulture.block.HorticultureBlocks;

import javax.annotation.Nonnull;
import java.util.Map;

public class StateMapperStump extends DefaultStateMapper {
    @Override
    @Nonnull
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        String s = (Block.REGISTRY.getNameForObject(state.getBlock())).toString();
        return new ModelResourceLocation(s, getPropertyString(map));
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block block) {
        for (int i = 0; i < 4; i++) {
            mapStateModelLocations.put(new BlockStateContainer(block).getBaseState(), new ModelResourceLocation(block.getRegistryName(), "mushroom_" + i));
        }

        return super.putStateModelLocations(block);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBaking(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        IBakedModel[] mushroom_models = new IBakedModel[4];
        for (int i = 0; i < mushroom_models.length; i++) {
            mushroom_models[i] = registry.getObject(new ModelResourceLocation("horticulture:stump", "mushroom_" + i));
        }

        for (IBlockState state : HorticultureBlocks.STUMP.getBlockState().getValidStates()) {
            ModelResourceLocation resource = getModelResourceLocation(state);
            registry.putObject(resource, new BakedStump(registry.getObject(resource), mushroom_models));
        }
    }
}

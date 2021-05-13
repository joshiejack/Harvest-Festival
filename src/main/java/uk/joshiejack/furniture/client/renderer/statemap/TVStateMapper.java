package uk.joshiejack.furniture.client.renderer.statemap;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.block.BlockTelevision;
import uk.joshiejack.furniture.block.FurnitureBlocks;
import uk.joshiejack.furniture.client.renderer.block.model.BakedTV;
import uk.joshiejack.furniture.television.TVProgram;

import javax.annotation.Nonnull;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Furniture.MODID, value = Side.CLIENT)
public class TVStateMapper extends DefaultStateMapper {
    public static final TVStateMapper INSTANCE = new TVStateMapper();

    @Override
    @Nonnull
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        String s = (Block.REGISTRY.getNameForObject(state.getBlock())).toString();
        return new ModelResourceLocation(s, getPropertyString(map));
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent event) {
        TVProgram.REGISTRY.values().forEach(program -> event.getMap().registerSprite(new ResourceLocation(program.getTVSprite())));
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@Nonnull Block block) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            mapStateModelLocations.put(new BlockStateContainer(block).getBaseState(), new ModelResourceLocation(block.getRegistryName(), "screen=" + facing.getName2()));
        }

        return super.putStateModelLocations(block);
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onBaking(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        Map<EnumFacing, IBakedModel> screens = Maps.newHashMap();
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            screens.put(facing, registry.getObject(new ModelResourceLocation("furniture:television", "screen=" + facing.getName2())));
        }

        for (IBlockState state : FurnitureBlocks.TELEVISION.getBlockState().getValidStates()) {
            ModelResourceLocation resource = INSTANCE.getModelResourceLocation(state);
            registry.putObject(resource, new BakedTV(registry.getObject(resource), screens.get(state.getValue(BlockTelevision.FACING))));
        }
    }
}

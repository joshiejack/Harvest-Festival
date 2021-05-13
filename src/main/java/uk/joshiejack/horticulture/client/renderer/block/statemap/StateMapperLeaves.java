package uk.joshiejack.horticulture.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.horticulture.block.BlockLeavesTemperate;
import uk.joshiejack.horticulture.block.BlockLeavesTropical;
import uk.joshiejack.horticulture.client.renderer.block.model.BakedLeaves;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static net.minecraft.block.BlockLeaves.CHECK_DECAY;
import static net.minecraft.block.BlockLeaves.DECAYABLE;
import static uk.joshiejack.horticulture.Horticulture.MODID;
import static uk.joshiejack.horticulture.block.HorticultureBlocks.LEAVES_TEMPERATE;
import static uk.joshiejack.horticulture.block.HorticultureBlocks.LEAVES_TROPICAL;

public class StateMapperLeaves extends DefaultStateMapper {
    private static final List<IProperty<?>> ignored = Lists.newArrayList();
    static {
        ignored.add(CHECK_DECAY);
        ignored.add(DECAYABLE);
    }

    @Override
    @Nonnull
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        Map< IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        String s = (Block.REGISTRY.getNameForObject(state.getBlock())).toString();
        ignored.forEach(map::remove);
        return new ModelResourceLocation(s, getPropertyString(map));
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent event) {
        event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/leaves/oak_black"));
        event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/leaves/jungle_black"));
    }

    @SubscribeEvent
    public void onBaking(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        IBakedModel oak = registry.getObject(new ModelResourceLocation("minecraft:oak_leaves#normal"));
        TextureAtlasSprite spriteOak = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("horticulture:blocks/leaves/oak_black");
        for (BlockLeavesTemperate.Fruit leaves: BlockLeavesTemperate.Fruit.values()) {
            IBlockState state = LEAVES_TEMPERATE.getStateFromEnum(leaves);
            registry.putObject(getModelResourceLocation(state), new BakedLeaves(registry.getObject(getModelResourceLocation(state)), oak, spriteOak));
        }

        IBakedModel jungle = registry.getObject(new ModelResourceLocation("minecraft:jungle_leaves#normal"));
        TextureAtlasSprite spriteJungle = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("horticulture:blocks/leaves/jungle_black");
        for (BlockLeavesTropical.Fruit leaves: BlockLeavesTropical.Fruit.values()) {
            IBlockState state = LEAVES_TROPICAL.getStateFromEnum(leaves);
            registry.putObject(getModelResourceLocation(state), new BakedLeaves(registry.getObject(getModelResourceLocation(state)), jungle, spriteJungle));
        }
    }
}

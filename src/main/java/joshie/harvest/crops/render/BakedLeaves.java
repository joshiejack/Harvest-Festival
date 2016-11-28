package joshie.harvest.crops.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.base.render.BakedHF;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockLeavesFruit.LeavesFruit;
import joshie.harvest.crops.block.BlockLeavesTropical.LeavesTropical;
import joshie.harvest.debug.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.block.BlockLeaves.CHECK_DECAY;
import static net.minecraft.block.BlockLeaves.DECAYABLE;

@SuppressWarnings("unused")
public class BakedLeaves extends BakedHF {
    private final TextureAtlasSprite sprite;
    private final IBakedModel base;
    private Season season;

    public BakedLeaves(IBakedModel parent, IBakedModel base, Season season, TextureAtlasSprite sprite) {
        super(parent);
        this.base = base;
        this.season = season;
        this.sprite = sprite;
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        List<BakedQuad> quads = new ArrayList<>();
        if (MCClientHelper.getMinecraft().gameSettings.fancyGraphics) base.getQuads(state, side, rand).stream().forEachOrdered(quads::add);
        else base.getQuads(state, side, rand).stream().map(quad -> new BakedQuadRetextured(quad, sprite)).forEachOrdered(quads::add);
        if (HFApi.calendar.getDate(MCClientHelper.getWorld()).getSeason() == season) {
            BakedLeaves.super.getQuads(state, side, rand).stream().map(BakedTintedQuad :: new).forEachOrdered(quads::add);
        }

        return quads;
    }

    @HFEvents
    @SuppressWarnings("unused")
    public static class LeavesMapper extends DefaultStateMapper {
        private static final List <IProperty<?>> ignored = Lists.newArrayList();
        static {
            ignored.add(CHECK_DECAY);
            ignored.add(DECAYABLE);
        }

        @Override
        @Nonnull
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            Map< IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
            String s = (Block.REGISTRY.getNameForObject(state.getBlock())).toString();
            for (IProperty<?> iproperty : ignored) {
                map.remove(iproperty);
            }

            return new ModelResourceLocation(s, getPropertyString(map));
        }

        @SubscribeEvent
        public void onStitch(TextureStitchEvent event) {
            event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/leaves_oak_black"));
            event.getMap().registerSprite(new ResourceLocation(MODID, "blocks/leaves_jungle_black"));
        }

        @SubscribeEvent
        public void onBaking(ModelBakeEvent event) {
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            StringBuilder builder = new StringBuilder();
            for (ModelResourceLocation list: registry.getKeys()) {
                builder.append(list);
                builder.append("\n");
            }

            Debug.save(builder);

            IBakedModel oak = registry.getObject(new ModelResourceLocation("minecraft:oak_leaves#normal"));
            TextureAtlasSprite spriteOak = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("harvestfestival:blocks/leaves_oak_black");
            for (LeavesFruit leaves: LeavesFruit.values()) {
                IBlockState state = HFCrops.LEAVES_FRUIT.getStateFromEnum(leaves);
                registry.putObject(getModelResourceLocation(state), new BakedLeaves(registry.getObject(getModelResourceLocation(state)), oak, leaves.getSeason(), spriteOak));
            }

            IBakedModel jungle = registry.getObject(new ModelResourceLocation("minecraft:jungle_leaves#normal"));
            TextureAtlasSprite spriteJungle = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("harvestfestival:blocks/leaves_jungle_black");
            for (LeavesTropical leaves: LeavesTropical.values()) {
                IBlockState state = HFCrops.LEAVES_TROPICAL.getStateFromEnum(leaves);
                registry.putObject(getModelResourceLocation(state), new BakedLeaves(registry.getObject(getModelResourceLocation(state)), oak, leaves.getSeason(), spriteJungle));
            }

            if (oak == null) throw new NullPointerException("Oak was null");
        }
    }
}


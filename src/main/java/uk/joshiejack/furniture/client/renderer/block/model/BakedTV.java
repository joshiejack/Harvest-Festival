package uk.joshiejack.furniture.client.renderer.block.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.furniture.block.properties.PropertyTVProgram;
import uk.joshiejack.furniture.television.TVProgram;
import uk.joshiejack.penguinlib.client.renderer.block.model.BakedPenguin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;


public class BakedTV extends BakedPenguin {
    private final Map<TVProgram, Map<EnumFacing, List<BakedQuad>>> models = Maps.newHashMap();
    private final IBakedModel screen;

    public BakedTV(IBakedModel parent, IBakedModel screen) {
        super(parent);
        this.screen = screen;
    }

    @Override
    public List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState extended = ((IExtendedBlockState)state);
            TVProgram program = extended.getValue(PropertyTVProgram.INSTANCE);
            if (program == null) program = TVProgram.OFF; //No Null!
            if (!models.containsKey(program)) {
                models.put(program, Maps.newHashMap());
            }


            Map<EnumFacing, List<BakedQuad>> map = models.get(program);
            if (!map.containsKey(side)) {
                List<BakedQuad> quads = Lists.newArrayList(super.getQuads(state, side, rand));
                TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(program.getTVSprite());
                screen.getQuads(state, side, rand).forEach(quad -> quads.add(new BakedQuadRetextured(quad, texture)));
                map.put(side, ImmutableList.copyOf(quads));
            }

            //Yes my love
            return map.get(side);
        } else return super.getQuads(state, side, rand);
    }

}

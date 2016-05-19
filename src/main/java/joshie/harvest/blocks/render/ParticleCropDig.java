package joshie.harvest.blocks.render;

import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCropDig extends ParticleDigging {
    public ParticleCropDig(TextureAtlasSprite icon, World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, IBlockState state) {
        super(world, posX, posY, posZ, motionX, motionY, motionZ, state);
        if (state.getBlock() != HFBlocks.CROPS) {
            this.setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.CARROTS.getDefaultState()));
        }

        this.setParticleTexture(icon);
    }
}
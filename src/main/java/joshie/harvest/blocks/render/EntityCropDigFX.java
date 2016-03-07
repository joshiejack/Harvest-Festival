package joshie.harvest.blocks.render;

import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityCropDigFX extends EntityDiggingFX {
    public EntityCropDigFX(IIcon icon, World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, Block block, int meta) {
        super(world, posX, posY, posZ, motionX, motionY, motionZ, block, meta);
        if (block != HFBlocks.crops) {
            this.setParticleIcon(Blocks.carrots.getIcon(0, 0));
        }

        this.setParticleIcon(icon);
    }
}

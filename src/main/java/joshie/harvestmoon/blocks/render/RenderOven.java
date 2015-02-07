package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.config.Client;
import joshie.harvestmoon.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderOven extends RenderBase {
    // \((.*),\s(.*),\s(.*),\s(.*),\s(.*),\s(.*)\)
    // \($3, $2, $1, $6, $5, $4\)

    @Override
    public void renderBlock() {
        if (Client.USE_MODERN_OVEN_RENDER) {
            if (isItem()) {
                GL11.glScalef(0.95F, 0.95F, 0.95F);
                GL11.glTranslated(0F, -0.05F, 0F);
            }

            if (dir == ForgeDirection.WEST || isItem()) {
                setTexture(Blocks.snow);
                renderBlock(0.95D, 0.1D, 0D, 1D, 0.95D, 1D);
                renderBlock(0.1D, 0.1D, 0D, 0.95D, 0.95D, 0.05D);
                renderBlock(0.1D, 0.1D, 0.95D, 0.95D, 0.95D, 1D);
                renderBlock(0.1D, 0.95D, 0D, 0.9D, 1D, 1D);
                renderBlock(0.1D, 0.1D, 0.05D, 0.95D, 0.15D, 0.95D);
                renderBlock(0.1D, 0.7D, 0.05D, 0.95D, 0.75D, 0.95D);
                renderBlock(0.9D, 0.95D, 0D, 1D, 1.1D, 1D);

                renderBlock(0.075D, 0.9D, 0.1D, 0.1D, 0.95D, 0.9D);
                renderBlock(0.075D, 0.65D, 0.1D, 0.1D, 0.75D, 0.9D);
                renderBlock(0.075D, 0.05D, 0.1D, 0.1D, 0.2D, 0.9D);
                renderBlock(0.075D, 0.05D, 0D, 0.1D, 0.95D, 0.1D);
                renderBlock(0.075D, 0.05D, 0.9D, 0.1D, 0.95D, 1D);

                setTexture(Blocks.iron_bars);
                renderBlock(0.125D, 0.3D, 0.05D, 0.95D, 0.31D, 0.95D);
                renderBlock(0.125D, 0.5D, 0.05D, 0.95D, 0.51D, 0.95D);

                setTexture(Blocks.glass);
                renderBlock(0.1D, 0.75D, 0.1D, 0.11D, 0.9D, 0.9D);
                renderBlock(0.1D, 0.15D, 0.1D, 0.11D, 0.65D, 0.9D);

                setTexture(Blocks.obsidian);
                renderBlock(0.2D, 1D, 0.05D, 0.9D, 1.01D, 0.95D);

                renderBlock(0.1D, 0D, 0.1D, 0.2D, 0.1D, 0.2D);
                renderBlock(0.8D, 0D, 0.1D, 0.9D, 0.1D, 0.2D);
                renderBlock(0.1D, 0D, 0.8D, 0.2D, 0.1D, 0.9D);
                renderBlock(0.8D, 0D, 0.8D, 0.9D, 0.1D, 0.9D);

                renderBlock(0.025D, 0.9D, 0.1D, 0.05D, 0.95D, 0.9D);
                renderBlock(0.025D, 0.9D, 0.1D, 0.075D, 0.95D, 0.2D);
                renderBlock(0.025D, 0.9D, 0.8D, 0.075D, 0.95D, 0.9D);

                renderBlock(0.025D, 0.2D, 0.9D, 0.05D, 0.7D, 0.95D);
                renderBlock(0.025D, 0.65D, 0.9D, 0.075D, 0.7D, 0.95D);
                renderBlock(0.025D, 0.2D, 0.9D, 0.075D, 0.25D, 0.95D);
            } else if (dir == ForgeDirection.NORTH) {
                setTexture(Blocks.snow);
                renderBlock(0D, 0.1D, 0.95D, 1D, 0.95D, 1D);
                renderBlock(0D, 0.1D, 0.1D, 0.05D, 0.95D, 0.95D);
                renderBlock(0.95D, 0.1D, 0.1D, 1D, 0.95D, 0.95D);
                renderBlock(0D, 0.95D, 0.1D, 1D, 1D, 0.9D);
                renderBlock(0.05D, 0.1D, 0.1D, 0.95D, 0.15D, 0.95D);
                renderBlock(0.05D, 0.7D, 0.1D, 0.95D, 0.75D, 0.95D);
                renderBlock(0D, 0.95D, 0.9D, 1D, 1.1D, 1D);

                renderBlock(0.1D, 0.9D, 0.075D, 0.9D, 0.95D, 0.1D);
                renderBlock(0.1D, 0.65D, 0.075D, 0.9D, 0.75D, 0.1D);
                renderBlock(0.1D, 0.05D, 0.075D, 0.9D, 0.2D, 0.1D);
                renderBlock(0D, 0.05D, 0.075D, 0.1D, 0.95D, 0.1D);
                renderBlock(0.9D, 0.05D, 0.075D, 1D, 0.95D, 0.1D);

                setTexture(Blocks.iron_bars);
                renderBlock(0.05D, 0.3D, 0.125D, 0.95D, 0.31D, 0.95D);
                renderBlock(0.05D, 0.5D, 0.125D, 0.95D, 0.51D, 0.95D);

                setTexture(Blocks.glass);
                renderBlock(0.1D, 0.75D, 0.1D, 0.9D, 0.9D, 0.11D);
                renderBlock(0.1D, 0.15D, 0.1D, 0.9D, 0.65D, 0.11D);

                setTexture(Blocks.obsidian);
                renderBlock(0.05D, 1D, 0.2D, 0.95D, 1.01D, 0.9D);

                renderBlock(0.1D, 0D, 0.1D, 0.2D, 0.1D, 0.2D);
                renderBlock(0.1D, 0D, 0.8D, 0.2D, 0.1D, 0.9D);
                renderBlock(0.8D, 0D, 0.1D, 0.9D, 0.1D, 0.2D);
                renderBlock(0.8D, 0D, 0.8D, 0.9D, 0.1D, 0.9D);

                renderBlock(0.1D, 0.9D, 0.025D, 0.9D, 0.95D, 0.05D);
                renderBlock(0.1D, 0.9D, 0.025D, 0.2D, 0.95D, 0.075D);
                renderBlock(0.8D, 0.9D, 0.025D, 0.9D, 0.95D, 0.075D);

                renderBlock(0.9D, 0.2D, 0.025D, 0.95D, 0.7D, 0.05D);
                renderBlock(0.9D, 0.65D, 0.025D, 0.95D, 0.7D, 0.075D);
                renderBlock(0.9D, 0.2D, 0.025D, 0.95D, 0.25D, 0.075D);
            } else if (dir == ForgeDirection.EAST) {
                setTexture(Blocks.snow);
                renderBlock(0D, 0.1D, 0.0D, 0.05D, 0.95D, 1.0D);
                renderBlock(0.05D, 0.1D, 0.0D, 0.9D, 0.95D, 0.05D);
                renderBlock(0.05D, 0.1D, 0.95D, 0.9D, 0.95D, 1.0D);
                renderBlock(0.099D, 0.95D, 0.0D, 0.9D, 1.0D, 1.0D);
                renderBlock(0.05D, 0.1D, 0.05D, 0.9D, 0.15D, 0.95D);
                renderBlock(0.05D, 0.7D, 0.05D, 0.9D, 0.75D, 0.95D);
                renderBlock(0D, 0.95D, 0.0D, 0.099D, 1.1D, 1.0D);
                renderBlock(0.9D, 0.9D, 0.1D, 0.925D, 0.95D, 0.9D);
                renderBlock(0.9D, 0.65D, 0.1D, 0.925D, 0.75D, 0.9D);
                renderBlock(0.9D, 0.05D, 0.1D, 0.925D, 0.2D, 0.9D);
                renderBlock(0.9D, 0.05D, 0.0D, 0.925D, 0.95D, 0.1D);
                renderBlock(0.9D, 0.05D, 0.9D, 0.925D, 0.95D, 1.0D);

                setTexture(Blocks.iron_bars);
                renderBlock(0.05D, 0.3D, 0.05D, 0.875D, 0.31D, 0.95D);
                renderBlock(0.05D, 0.5D, 0.05D, 0.875D, 0.51D, 0.95D);

                setTexture(Blocks.glass);
                renderBlock(0.89D, 0.75D, 0.1D, 0.9D, 0.9D, 0.9D);
                renderBlock(0.89D, 0.15D, 0.1D, 0.9D, 0.65D, 0.9D);

                setTexture(Blocks.obsidian);
                renderBlock(0.099D, 1.0D, 0.05D, 0.8D, 1.01D, 0.95D);
                renderBlock(0.8D, 0.0D, 0.1D, 0.9D, 0.1D, 0.2D);
                renderBlock(0.099D, 0.0D, 0.1D, 0.199D, 0.1D, 0.2D);
                renderBlock(0.8D, 0.0D, 0.8D, 0.9D, 0.1D, 0.9D);
                renderBlock(0.099D, 0.0D, 0.8D, 0.199D, 0.1D, 0.9D);
                renderBlock(0.95D, 0.9D, 0.1D, 0.975D, 0.95D, 0.9D);
                renderBlock(0.925D, 0.9D, 0.1D, 0.975D, 0.95D, 0.2D);
                renderBlock(0.925D, 0.9D, 0.8D, 0.975D, 0.95D, 0.9D);
                renderBlock(0.95D, 0.2D, 0.9D, 0.975D, 0.7D, 0.95D);
                renderBlock(0.925D, 0.65D, 0.9D, 0.975D, 0.7D, 0.95D);
                renderBlock(0.925D, 0.2D, 0.9D, 0.975D, 0.25D, 0.95D);
            } else if (dir == ForgeDirection.SOUTH) {
                setTexture(Blocks.snow);
                renderBlock(0.0D, 0.1D, 0D, 1.0D, 0.95D, 0.05D);
                renderBlock(0.0D, 0.1D, 0.05D, 0.05D, 0.95D, 0.9D);
                renderBlock(0.95D, 0.1D, 0.05D, 1.0D, 0.95D, 0.9D);
                renderBlock(0.0D, 0.95D, 0.099D, 1.0D, 1.0D, 0.9D);
                renderBlock(0.05D, 0.1D, 0.05D, 0.95D, 0.15D, 0.9D);
                renderBlock(0.05D, 0.7D, 0.05D, 0.95D, 0.75D, 0.9D);
                renderBlock(0.0D, 0.95D, 0D, 1.0D, 1.1D, 0.099D);
                renderBlock(0.1D, 0.9D, 0.9D, 0.9D, 0.95D, 0.925D);
                renderBlock(0.1D, 0.65D, 0.9D, 0.9D, 0.75D, 0.925D);
                renderBlock(0.1D, 0.05D, 0.9D, 0.9D, 0.2D, 0.925D);
                renderBlock(0.0D, 0.05D, 0.9D, 0.1D, 0.95D, 0.925D);
                renderBlock(0.9D, 0.05D, 0.9D, 1.0D, 0.95D, 0.925D);

                setTexture(Blocks.iron_bars);
                renderBlock(0.05D, 0.3D, 0.05D, 0.95D, 0.31D, 0.875D);
                renderBlock(0.05D, 0.5D, 0.05D, 0.95D, 0.51D, 0.875D);

                setTexture(Blocks.glass);
                renderBlock(0.1D, 0.75D, 0.89D, 0.9D, 0.9D, 0.9D);
                renderBlock(0.1D, 0.15D, 0.89D, 0.9D, 0.65D, 0.9D);

                setTexture(Blocks.obsidian);
                renderBlock(0.05D, 1.0D, 0.099D, 0.95D, 1.01D, 0.8D);
                renderBlock(0.1D, 0.0D, 0.8D, 0.2D, 0.1D, 0.9D);
                renderBlock(0.1D, 0.0D, 0.099D, 0.2D, 0.1D, 0.199D);
                renderBlock(0.8D, 0.0D, 0.8D, 0.9D, 0.1D, 0.9D);
                renderBlock(0.8D, 0.0D, 0.099D, 0.9D, 0.1D, 0.199D);
                renderBlock(0.1D, 0.9D, 0.95D, 0.9D, 0.95D, 0.975D);
                renderBlock(0.1D, 0.9D, 0.925D, 0.2D, 0.95D, 0.975D);
                renderBlock(0.8D, 0.9D, 0.925D, 0.9D, 0.95D, 0.975D);
                renderBlock(0.9D, 0.2D, 0.95D, 0.95D, 0.7D, 0.975D);
                renderBlock(0.9D, 0.65D, 0.925D, 0.95D, 0.7D, 0.975D);
                renderBlock(0.9D, 0.2D, 0.925D, 0.95D, 0.25D, 0.975D);
            }
        }
    }
}
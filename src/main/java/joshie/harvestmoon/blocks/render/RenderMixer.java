package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.core.config.Client;
import joshie.harvestmoon.core.util.RenderBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderMixer extends RenderBase {
    @Override
    public void renderBlock() {
        if(Client.USE_MODERN_MIXER_RENDER) {
            setTexture(Blocks.iron_block);
            if (dir == ForgeDirection.WEST || isItem()) {
                renderBlock(0.025D, 0.25D, 0.71D, 0.045D, 0.449D, 0.73D);
                renderBlock(0.045D, 0.199D, 0.73D, 0.475D, 0.449D, 0.75D);
                renderBlock(0.575D, 0.199D, 0.375D, 0.924D, 0.35D, 0.625D);
                renderBlock(0.005D, 0.25D, 0.28D, 0.025D, 0.449D, 0.72D);
                renderBlock(0.025D, 0.25D, 0.27D, 0.045D, 0.449D, 0.29D);
                renderBlock(0.2D, 0.099D, 0.45D, 0.3D, 0.199D, 0.55D);
                renderBlock(0.025D, 0.15D, 0.28D, 0.045D, 0.3D, 0.72D);
                renderBlock(0.45D, 0.0D, 0.35D, 0.95D, 0.099D, 0.649D);
                renderBlock(0.045D, 0.099D, 0.65D, 0.475D, 0.199D, 0.7D);
                renderBlock(0.07D, 0.0D, 0.3D, 0.45D, 0.099D, 0.7D);
                renderBlock(0.495D, 0.25D, 0.28D, 0.515D, 0.449D, 0.72D);
                renderBlock(0.6D, 0.35D, 0.4D, 0.899D, 0.5D, 0.6D);
                renderBlock(0.475D, 0.299D, 0.27D, 0.495D, 0.449D, 0.29D);
                renderBlock(0.2D, 0.75D, 0.375D, 0.6D, 0.85D, 0.575D);
                renderBlock(0.55D, 0.099D, 0.35D, 0.95D, 0.199D, 0.649D);
                renderBlock(0.475D, 0.099D, 0.3D, 0.505D, 0.199D, 0.7D);
                renderBlock(0.7D, 0.099D, 0.66D, 0.799D, 0.199D, 0.67D);
                renderBlock(0.12D, 0.5D, 0.35D, 0.77D, 0.65D, 0.649D);
                renderBlock(0.495D, 0.15D, 0.28D, 0.515D, 0.3D, 0.72D);
                renderBlock(0.145D, 0.65D, 0.375D, 0.745D, 0.75D, 0.625D);
                renderBlock(0.125D, 0.5D, 0.375D, 0.375D, 0.6D, 0.625D);
                renderBlock(0.475D, 0.299D, 0.71D, 0.495D, 0.449D, 0.73D);
                renderBlock(0.15D, 0.4D, 0.4D, 0.35D, 0.5D, 0.6D);
                renderBlock(0.045D, 0.1D, 0.27D, 0.495D, 0.3D, 0.3D);
                renderBlock(0.045D, 0.1D, 0.7D, 0.495D, 0.3D, 0.73D);
                renderBlock(0.045D, 0.199D, 0.25D, 0.475D, 0.449D, 0.27D);
                renderBlock(0.225D, 0.199D, 0.475D, 0.275D, 0.4D, 0.525D);
                renderBlock(0.045D, 0.099D, 0.3D, 0.475D, 0.199D, 0.35D);
                renderBlock(0.04D, 0.049D, 0.3D, 0.07D, 0.199D, 0.7D);
            } else if (dir == ForgeDirection.NORTH) {
                renderBlock(0.71D, 0.25D, 0.025D, 0.73D, 0.449D, 0.045D);
                renderBlock(0.73D, 0.199D, 0.045D, 0.75D, 0.449D, 0.475D);
                renderBlock(0.375D, 0.199D, 0.575D, 0.625D, 0.35D, 0.924D);
                renderBlock(0.28D, 0.25D, 0.005D, 0.72D, 0.449D, 0.025D);
                renderBlock(0.27D, 0.25D, 0.025D, 0.29D, 0.449D, 0.045D);
                renderBlock(0.45D, 0.099D, 0.2D, 0.55D, 0.199D, 0.3D);
                renderBlock(0.28D, 0.15D, 0.025D, 0.72D, 0.3D, 0.045D);
                renderBlock(0.35D, 0.0D, 0.45D, 0.649D, 0.099D, 0.95D);
                renderBlock(0.65D, 0.099D, 0.045D, 0.7D, 0.199D, 0.475D);
                renderBlock(0.3D, 0.0D, 0.07D, 0.7D, 0.099D, 0.45D);
                renderBlock(0.28D, 0.25D, 0.495D, 0.72D, 0.449D, 0.515D);
                renderBlock(0.4D, 0.35D, 0.6D, 0.6D, 0.5D, 0.899D);
                renderBlock(0.27D, 0.299D, 0.475D, 0.29D, 0.449D, 0.495D);
                renderBlock(0.375D, 0.75D, 0.2D, 0.575D, 0.85D, 0.6D);
                renderBlock(0.35D, 0.099D, 0.55D, 0.649D, 0.199D, 0.95D);
                renderBlock(0.3D, 0.099D, 0.475D, 0.7D, 0.199D, 0.505D);
                renderBlock(0.66D, 0.099D, 0.7D, 0.67D, 0.199D, 0.799D);
                renderBlock(0.35D, 0.5D, 0.12D, 0.649D, 0.65D, 0.77D);
                renderBlock(0.28D, 0.15D, 0.495D, 0.72D, 0.3D, 0.515D);
                renderBlock(0.375D, 0.65D, 0.145D, 0.625D, 0.75D, 0.745D);
                renderBlock(0.375D, 0.5D, 0.125D, 0.625D, 0.6D, 0.375D);
                renderBlock(0.71D, 0.299D, 0.475D, 0.73D, 0.449D, 0.495D);
                renderBlock(0.4D, 0.4D, 0.15D, 0.6D, 0.5D, 0.35D);
                renderBlock(0.27D, 0.1D, 0.045D, 0.3D, 0.3D, 0.495D);
                renderBlock(0.7D, 0.1D, 0.045D, 0.73D, 0.3D, 0.495D);
                renderBlock(0.25D, 0.199D, 0.045D, 0.27D, 0.449D, 0.475D);
                renderBlock(0.475D, 0.199D, 0.225D, 0.525D, 0.4D, 0.275D);
                renderBlock(0.3D, 0.099D, 0.045D, 0.35D, 0.199D, 0.475D);
                renderBlock(0.3D, 0.049D, 0.04D, 0.7D, 0.199D, 0.07D);
            } else if (dir == ForgeDirection.EAST) {
               renderBlock(0.955D, 0.25D, 0.71D, 0.975D, 0.449D, 0.73D);
               renderBlock(0.525D, 0.199D, 0.73D, 0.955D, 0.449D, 0.75D);
               renderBlock(0.075D, 0.199D, 0.375D, 0.425D, 0.35D, 0.625D);
               renderBlock(0.975D, 0.25D, 0.28D, 0.995D, 0.449D, 0.72D);
               renderBlock(0.955D, 0.25D, 0.27D, 0.975D, 0.449D, 0.29D);
               renderBlock(0.7D, 0.099D, 0.45D, 0.8D, 0.199D, 0.55D);
               renderBlock(0.955D, 0.15D, 0.28D, 0.975D, 0.3D, 0.72D);
               renderBlock(0.05D, 0.0D, 0.35D, 0.55D, 0.099D, 0.649D);
               renderBlock(0.525D, 0.099D, 0.65D, 0.955D, 0.199D, 0.7D);
               renderBlock(0.55D, 0.0D, 0.3D, 0.929D, 0.099D, 0.7D);
               renderBlock(0.485D, 0.25D, 0.28D, 0.505D, 0.449D, 0.72D);
               renderBlock(0.1D, 0.35D, 0.4D, 0.4D, 0.5D, 0.6D);
               renderBlock(0.505D, 0.299D, 0.27D, 0.525D, 0.449D, 0.29D);
               renderBlock(0.4D, 0.75D, 0.375D, 0.8D, 0.85D, 0.575D);
               renderBlock(0.05D, 0.099D, 0.35D, 0.449D, 0.199D, 0.649D);
               renderBlock(0.495D, 0.099D, 0.3D, 0.525D, 0.199D, 0.7D);
               renderBlock(0.2D, 0.099D, 0.66D, 0.3D, 0.199D, 0.67D);
               renderBlock(0.229D, 0.5D, 0.35D, 0.88D, 0.65D, 0.649D);
               renderBlock(0.485D, 0.15D, 0.28D, 0.505D, 0.3D, 0.72D);
               renderBlock(0.255D, 0.65D, 0.375D, 0.855D, 0.75D, 0.625D);
               renderBlock(0.625D, 0.5D, 0.375D, 0.875D, 0.6D, 0.625D);
               renderBlock(0.505D, 0.299D, 0.71D, 0.525D, 0.449D, 0.73D);
               renderBlock(0.65D, 0.4D, 0.4D, 0.85D, 0.5D, 0.6D);
               renderBlock(0.505D, 0.1D, 0.27D, 0.955D, 0.3D, 0.3D);
               renderBlock(0.505D, 0.1D, 0.7D, 0.955D, 0.3D, 0.73D);
               renderBlock(0.525D, 0.199D, 0.25D, 0.955D, 0.449D, 0.27D);
               renderBlock(0.725D, 0.199D, 0.475D, 0.775D, 0.4D, 0.525D);
               renderBlock(0.525D, 0.099D, 0.3D, 0.955D, 0.199D, 0.35D);
               renderBlock(0.929D, 0.049D, 0.3D, 0.96D, 0.199D, 0.7D);
            } else if (dir == ForgeDirection.SOUTH) {
               renderBlock(0.71D, 0.25D, 0.955D, 0.73D, 0.449D, 0.975D);
               renderBlock(0.73D, 0.199D, 0.525D, 0.75D, 0.449D, 0.955D);
               renderBlock(0.375D, 0.199D, 0.075D, 0.625D, 0.35D, 0.425D);
               renderBlock(0.28D, 0.25D, 0.975D, 0.72D, 0.449D, 0.995D);
               renderBlock(0.27D, 0.25D, 0.955D, 0.29D, 0.449D, 0.975D);
               renderBlock(0.45D, 0.099D, 0.7D, 0.55D, 0.199D, 0.8D);
               renderBlock(0.28D, 0.15D, 0.955D, 0.72D, 0.3D, 0.975D);
               renderBlock(0.35D, 0.0D, 0.05D, 0.649D, 0.099D, 0.55D);
               renderBlock(0.65D, 0.099D, 0.525D, 0.7D, 0.199D, 0.955D);
               renderBlock(0.3D, 0.0D, 0.55D, 0.7D, 0.099D, 0.929D);
               renderBlock(0.28D, 0.25D, 0.485D, 0.72D, 0.449D, 0.505D);
               renderBlock(0.4D, 0.35D, 0.1D, 0.6D, 0.5D, 0.4D);
               renderBlock(0.27D, 0.299D, 0.505D, 0.29D, 0.449D, 0.525D);
               renderBlock(0.375D, 0.75D, 0.4D, 0.575D, 0.85D, 0.8D);
               renderBlock(0.35D, 0.099D, 0.05D, 0.649D, 0.199D, 0.449D);
               renderBlock(0.3D, 0.099D, 0.495D, 0.7D, 0.199D, 0.525D);
               renderBlock(0.66D, 0.099D, 0.2D, 0.67D, 0.199D, 0.3D);
               renderBlock(0.35D, 0.5D, 0.229D, 0.649D, 0.65D, 0.88D);
               renderBlock(0.28D, 0.15D, 0.485D, 0.72D, 0.3D, 0.505D);
               renderBlock(0.375D, 0.65D, 0.255D, 0.625D, 0.75D, 0.855D);
               renderBlock(0.375D, 0.5D, 0.625D, 0.625D, 0.6D, 0.875D);
               renderBlock(0.71D, 0.299D, 0.505D, 0.73D, 0.449D, 0.525D);
               renderBlock(0.4D, 0.4D, 0.65D, 0.6D, 0.5D, 0.85D);
               renderBlock(0.27D, 0.1D, 0.505D, 0.3D, 0.3D, 0.955D);
               renderBlock(0.7D, 0.1D, 0.505D, 0.73D, 0.3D, 0.955D);
               renderBlock(0.25D, 0.199D, 0.525D, 0.27D, 0.449D, 0.955D);
               renderBlock(0.475D, 0.199D, 0.725D, 0.525D, 0.4D, 0.775D);
               renderBlock(0.3D, 0.099D, 0.525D, 0.35D, 0.199D, 0.955D);
               renderBlock(0.3D, 0.049D, 0.929D, 0.7D, 0.199D, 0.96D);
            }
        }
    }
}

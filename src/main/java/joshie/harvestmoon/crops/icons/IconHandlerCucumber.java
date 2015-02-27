package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.crops.ICropIconHandler;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerCucumber implements ICropIconHandler {
    private IIcon[] stageIcons;

    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(int stage) {
        if (stage <= 4) return stageIcons[0];
        else if (stage <= 7) return stageIcons[1];
        else if (stage <= 9) return stageIcons[2];
        else return stageIcons[3];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[4];
        for (int i = 0; i < stageIcons.length; i++) {
            stageIcons[i] = register.registerIcon(HMModInfo.CROPPATH + HMCrops.cucumber.getUnlocalizedName() + "_" + (i + 1));
        }
    }
}

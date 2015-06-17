package joshie.harvest.crops.icons;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.HFCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerCabbage extends AbstractIconHandler {

    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (stage <= 4) return stageIcons[0];
        else if (stage <= 9) return stageIcons[1];
        else if (stage <= 14) return stageIcons[2];
        else return stageIcons[3];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[4];
        for (int i = 0; i < stageIcons.length; i++) {
            stageIcons[i] = register.registerIcon(HFModInfo.CROPPATH + HFCrops.cabbage.getUnlocalizedName() + "_" + (i + 1));
        }
    }
}

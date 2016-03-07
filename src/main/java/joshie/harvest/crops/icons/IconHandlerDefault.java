package joshie.harvest.crops.icons;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.Crop;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IconHandlerDefault extends AbstractIconHandler {
    protected Crop crop;

    public IconHandlerDefault(Crop crop) {
        this.crop = crop;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(PlantSection section, int stage) {
        return stageIcons[Math.max(0, stage - 1)];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        stageIcons = new IIcon[crop.getStages()];
        for (int i = 0; i < crop.getStages(); i++) {
            stageIcons[i] = register.registerIcon(HFModInfo.CROPPATH + crop.getUnlocalizedName() + "/stage_" + i);
        }
    }
}

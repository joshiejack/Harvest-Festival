package joshie.harvest.crops.icons;

import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IconHandlerSeedFood extends AbstractIconHandler {
    private Block block;

    public IconHandlerSeedFood(Block block) {
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (stage == 1) return block.getIcon(0, 0);
        if (stage == 2 || stage == 3 || stage == 4) return block.getIcon(0, 2);
        if (stage == 5 || stage == 6 || stage == 7) return block.getIcon(0, 4);
        return stage == 8 ? block.getIcon(0, 7) : block.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }
}

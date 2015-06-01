package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockBase;
import net.minecraft.block.material.Material;

public abstract class BlockHFBase extends BlockBase {
    protected BlockHFBase(Material material) {
        super(material, HFModInfo.MODPATH, HFTab.tabFarming);
    }
}

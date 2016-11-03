package joshie.harvest.fishing.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.fishing.block.BlockFishing.FishingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class BlockFishing extends BlockHFEnum<BlockFishing, FishingBlock> {
    public BlockFishing() {
        super(Material.WOOD, FishingBlock.class, HFTab.FISHING);
    }

    public enum FishingBlock implements IStringSerializable {
        TRAP;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}

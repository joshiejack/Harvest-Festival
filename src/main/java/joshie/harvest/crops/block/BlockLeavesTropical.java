package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFLeaves;
import joshie.harvest.crops.block.BlockLeavesTropical.LeavesTropical;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class BlockLeavesTropical extends BlockHFLeaves<BlockLeavesTropical, LeavesTropical> {
    @SuppressWarnings("ConstantConditions")
    public BlockLeavesTropical() {
        super(LeavesTropical.class);
        setCreativeTab(null);
    }

    public enum LeavesTropical implements IStringSerializable {
        BANANA;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}

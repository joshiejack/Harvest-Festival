package joshie.harvest.crops.block;

import joshie.harvest.core.base.block.BlockHFLeaves;
import joshie.harvest.crops.block.BlockLeavesFruit.LeavesFruit;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class BlockLeavesFruit extends BlockHFLeaves<BlockLeavesFruit, LeavesFruit> {
    @SuppressWarnings("ConstantConditions")
    public BlockLeavesFruit() {
        super(LeavesFruit.class);
        setCreativeTab(null);
    }

    public enum LeavesFruit implements IStringSerializable {
        APPLE, GRAPE, ORANGE, PEACH;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}

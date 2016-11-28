package joshie.harvest.crops.block;

import joshie.harvest.api.calendar.Season;
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
        BANANA(Season.SUMMER);

        private final Season season;
        LeavesTropical(Season season) {
            this.season = season;
        }

        public Season getSeason() {
            return season;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}

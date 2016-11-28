package joshie.harvest.crops.block;

import joshie.harvest.api.calendar.Season;
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
        APPLE(Season.AUTUMN), GRAPE(Season.AUTUMN), ORANGE(Season.SUMMER), PEACH(Season.SUMMER);

        private final Season season;
        LeavesFruit(Season season) {
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

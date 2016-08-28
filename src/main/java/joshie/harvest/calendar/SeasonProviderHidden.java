package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.SeasonProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class SeasonProviderHidden implements SeasonProvider {
    @SideOnly(Side.CLIENT)
    @Override
    public boolean displayHUD() {
        return false;
    }

    @Override
    public Season getSeasonAtPos(World world, @Nullable BlockPos pos) {
        return null; //Don't even bother checking
    }
}

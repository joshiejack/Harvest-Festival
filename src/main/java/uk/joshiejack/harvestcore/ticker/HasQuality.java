package uk.joshiejack.harvestcore.ticker;

import uk.joshiejack.harvestcore.registry.Quality;

import javax.annotation.Nullable;

public interface HasQuality {
    @Nullable
    Quality getQuality();
}

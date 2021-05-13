package uk.joshiejack.harvestcore.world.mine;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import uk.joshiejack.harvestcore.database.MineLoader;
import uk.joshiejack.harvestcore.world.mine.dimension.MineWorldProvider;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;

import java.util.List;
import java.util.Objects;

public class Mine {
    public static final Int2ObjectMap<Mine> BY_ID = new Int2ObjectOpenHashMap<>();
    public static final Object2IntMap<String> BY_NAME = new Object2IntOpenHashMap<>();
    public final DimensionType type;
    private final String exitPoint;
    private final List<String> tiers;
    public Tier[] TIERS = new Tier[0];

    private Mine(DimensionType type, String exitPoint, List<String> tiers) {
        this.type = type;
        this.exitPoint = exitPoint;
        this.tiers = tiers;
    }

    public static Mine create(String name, String nameID, String exitPoint, int dimension, List<String> tiers) {
        Mine mine = new Mine(DimensionType.register(name, "_" + nameID, dimension, MineWorldProvider.class, false), exitPoint, tiers);
        BY_ID.put(dimension, mine);
        BY_NAME.put(nameID, dimension);
        return mine;
    }

    public boolean init() {
        DimensionManager.registerDimension(type.getId(), type);
        tiers.stream().map(t -> MineLoader.TIERS.get(new ResourceLocation(t)))
                .filter(Objects::nonNull)
                .forEach(tier -> {
                    int index = TIERS.length;
                    Tier[] tmp = new Tier[index + 1];
                    System.arraycopy(TIERS, 0, tmp, 0, TIERS.length);
                    TIERS = tmp;
                    TIERS[index] = tier;
        });

        return true;
    }

    public String getExitPoint() {
        return exitPoint;
    }

    public Tier getTierFromInt(int num) {
        return num < 0 ? TIERS[0] : num < TIERS.length ? TIERS[num] : TIERS[TIERS.length - 1];
    }
}

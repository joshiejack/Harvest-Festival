package uk.joshiejack.harvestcore.world.mine.tier;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.database.MineLoader;
import uk.joshiejack.harvestcore.world.mine.dimension.MineDecorator;
import uk.joshiejack.harvestcore.world.mine.dimension.floors.RoomGenerator;
import uk.joshiejack.harvestcore.world.mine.dimension.floors.RoomGeneratorTemplate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;

public class TierBuilder implements Comparable<TierBuilder> {
    public ResourceLocation registryName;
    private ResourceLocation hud;
    private ResourceLocation biome;
    private IBlockState floor;
    private IBlockState wall;
    private IBlockState ladder;
    private IBlockState upper_portal;
    private IBlockState lower_portal;
    private Decorator[] decorators;
    private Loot[] loot;
    private Special[] special_loot;
    private Entities[] entities;
    private String[] generators;
    private NamedRanges[] ranges;
    private int order;

    public TierBuilder() {}

    @Override
    public int compareTo(@Nonnull TierBuilder o) {
        return Integer.compare(order, o.order);
    }

    public static class NamedRanges {
        public String name;
        public int min_floor, max_floor;
        public IBlockState floor;
        public IBlockState wall;
    }

    public static class Decorator {
        public String type;
        public String[] ranges;
        public IBlockState state;
        public int passes = 1;
        public int number = -1;
        public int chance = 0;
        public int floorMultiplier = -1;

        public int getPasses(int floor) {
            return floorMultiplier != -1 ? ((passes - floor) * floorMultiplier) : passes;
        }
    }

    public static class Loot {
        private IBlockState state;
        private int min_floor = 1;
        private int max_floor = Integer.MAX_VALUE;
        private int cluster_distance;
        private int cluster_amount;
        private double weight;
    }

    public static class Entities {
        private ResourceLocation entity;
        private EnumCreatureType type = EnumCreatureType.MONSTER;
        private String[] ranges;
        private int weight;
        private int min_group = 1;
        private int max_group = 1;
    }

    public static class Special {
        private IBlockState state;
        private int negative;
        private double divider;
    }

    @SuppressWarnings("ConstantConditions, unchecked")
    public Tier build() {
        TierCustom tier = new TierCustom(Biome.REGISTRY.getObject(biome), decorators);
        tier.floor = floor;
        tier.wall = wall;
        tier.ladder = ladder;
        tier.upper_portal = upper_portal;
        tier.lower_portal = lower_portal;
        if (hud != null) tier.hud = hud;
        tier.addRanges(ranges);
        for (Loot loot: this.loot) {
            if (loot.state != null) {
                MineDecorator.GENERATED.add(loot.state.getBlock());
                tier.addLootedDecorator(loot.state, loot.min_floor, loot.max_floor, loot.weight)
                        .setClustered(loot.cluster_distance, loot.cluster_amount); //If distance is 0 it doesn't matter
            }
        }

        if (this.special_loot != null) {
            for (Special special : this.special_loot) {
                if (special.state != null) {
                    MineDecorator.GENERATED.add(special.state.getBlock());
                    tier.addSpecialLootDecorator(special.state, special.negative, special.divider);
                }
            }
        }

        for (String generator: generators) {
            RoomGenerator rg = MineLoader.getRoom(generator);
            if (rg == null) {
                HarvestCore.logger.log(Level.ERROR, "Couldn't find the generator: " + generator);
            } else {
                if (rg instanceof RoomGeneratorTemplate) {
                    for (RoomGeneratorTemplate extra : ((RoomGeneratorTemplate) rg).extra()) {
                        tier.addGenerator(extra);
                    }
                }

                tier.addGenerator(rg);
            }
        }

        for (Entities entity: entities) {
            tier.getSpawnableList(entity.type).add(
                    new Biome.SpawnListEntry((Class<? extends EntityLiving>) EntityList.getClass(entity.entity),
                            entity.weight, entity.min_group, entity.max_group));
            tier.addEntityRange(entity.entity, entity.ranges);
        }

        return tier;
    }
}

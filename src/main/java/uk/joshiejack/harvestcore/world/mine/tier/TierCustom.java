package uk.joshiejack.harvestcore.world.mine.tier;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.world.mine.dimension.decorators.*;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class TierCustom extends Tier {
    private static final Map<String, AbstractDecorator> decoratorTypes = Maps.newHashMap();
    private final Map<String, Pair<Integer, Integer>> ranges = Maps.newHashMap();
    private final HashMultimap<ResourceLocation, String> acceptedEntityRanges = HashMultimap.create();
    private final Cache<Integer, String> namedRanges = CacheBuilder.newBuilder().build();
    private final Map<String, IBlockState> floors = Maps.newHashMap();
    private final Map<String, IBlockState> walls = Maps.newHashMap();
    private final List<TierBuilder.Decorator> decorators;
    private final List<DecoratorLoot.Special> specialLoots = Lists.newArrayList();
    private final Biome biome;
    static {
        decoratorTypes.put("pillar", new DecoratorPillar());
        decoratorTypes.put("mushroom", new DecoratorMushroom());
        decoratorTypes.put("leaves", new DecoratorLeaves());
        decoratorTypes.put("grass", new DecoratorTallGrass());
        decoratorTypes.put("floor", new DecoratorFloor());
        decoratorTypes.put("carpet", new DecoratorFlooring());
        decoratorTypes.put("snow_layer", new DecoratorSnowLayer());
        decoratorTypes.put("icycles", new DecoratorIcycles());
        decoratorTypes.put("glowstone", new DecoratorGlowstone());
    }

    public TierCustom(Biome biome, TierBuilder.Decorator... decorators) {
        this.biome = biome;
        this.decorators = decorators != null ? Lists.newArrayList(decorators) : Lists.newArrayList(); //Empty list
    }

    public void addSpecialLootDecorator(IBlockState state, int negative, double divider) {
        specialLoots.add(new DecoratorLoot.Special(state, negative, divider));
    }

    public void addRanges(TierBuilder.NamedRanges[] ranges) {
        for (TierBuilder.NamedRanges range: ranges) {
            this.ranges.put(range.name, Pair.of(range.min_floor, range.max_floor));
            this.floors.put(range.name, range.floor == null ? floor : range.floor);
            this.walls.put(range.name, range.wall == null ? wall : range.wall);
        }
    }

    public void addEntityRange(ResourceLocation entity, String[] ranges) {
        acceptedEntityRanges.get(entity).addAll(Lists.newArrayList(ranges));
    }

    private String getRangeNameFromFloor(int floor) {
        try {
            return namedRanges.get(floor, () -> {
                for (Map.Entry<String, Pair<Integer, Integer>> pair: this.ranges.entrySet()) {
                    if (floor >= pair.getValue().getLeft() && floor <= pair.getValue().getRight()) return pair.getKey();
                }

                return Strings.EMPTY;
            });
        } catch (ExecutionException e) {
            return Strings.EMPTY;
        }
    }

    @Override
    public boolean canSpawn(EntityLiving entity, int floor) {
        return acceptedEntityRanges.get(EntityList.getKey(entity)).contains(getRangeNameFromFloor(floor));
    }

    @Override
    public Biome getBiome() {
        return biome;
    }

    @Override
    public IBlockState getFloor(int floor) {
        return floors.get(getRangeNameFromFloor(floor));
    }

    @Override
    public IBlockState getWall(int floor) {
        return walls.get(getRangeNameFromFloor(floor));
    }

    @Override
    protected void decorate(AbstractDecoratorWrapper world, BlockPos pos) {
        String ranges = getRangeNameFromFloor(world.floor);
        for (TierBuilder.Decorator decorator: decorators) {
            for (String r: decorator.ranges) {
                if (r.equals(ranges)) {
                    AbstractDecorator gen = decoratorTypes.get(decorator.type);
                    if (gen instanceof AbstractDecoratorModifyable) {
                        AbstractDecoratorModifyable modifyable = ((AbstractDecoratorModifyable)gen);
                        if (decorator.state != null) modifyable.setState(decorator.state.withRotation(Rotation.values()[world.rand.nextInt(Rotation.values().length)])); //Random facing
                        if (decorator.number != -1) modifyable.setNumber(decorator.number);
                    }

                    if (decorator.chance == 0 || world.rand.nextInt(decorator.chance) == 0) {
                        for (int i = 0; i < decorator.getPasses(world.floor); i++) {
                            int k = world.rand.nextInt(16) + 8;
                            int l = world.rand.nextInt(16) + 8;
                            BlockPos blockpos = pos.add(k, 0, l);
                            gen.decorate(world, blockpos);
                        }
                    }

                    //We exit this loop as we have generated
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public DecoratorLoot getLoot(Random rand, int floor, int actualFloor) {
        for (DecoratorLoot.Special special: specialLoots) {
            double bonus = ((double)(actualFloor - special.negative)) / special.divider;
            if (rand.nextDouble() <= bonus) return special.loot;
        }

        return super.getLoot(rand, floor, actualFloor);
    }
}

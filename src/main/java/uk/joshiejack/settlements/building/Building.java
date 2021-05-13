package uk.joshiejack.settlements.building;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.BuildingMaterialsCalculator;
import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.Template;
import uk.joshiejack.penguinlib.template.blocks.PlaceableWaypoint;
import uk.joshiejack.penguinlib.template.entities.PlaceableLiving;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.GsonHelper;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static uk.joshiejack.settlements.Settlements.MODID;

public class Building extends PenguinRegistry.Item<Building> {
    public static final ResourceLocation NPCS = new ResourceLocation(MODID, "npc");
    public static final Map<ResourceLocation, Building> REGISTRY = Maps.newHashMap();
    public static final Building NULL = new Building(new ResourceLocation(MODID, "null"), Template.wrap());
    private final Map<String, BlockPos> waypoints = Maps.newHashMap();
    private final List<NPC> residents;
    private ResourceLocation dailyScript;
    private final Template.WrappedTemplate template;
    private int limit;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

    public Building(ResourceLocation id, @Nonnull Template.WrappedTemplate template) {
        super("building", REGISTRY, id);
        this.template = template;
        this.residents = Lists.newArrayList();
    }

    private ResourceLocation temp_r;
    private boolean overrides;

    public Building setUpgrade(ResourceLocation resourceLocation, boolean overrides) {
        this.temp_r = resourceLocation;
        this.overrides = overrides;
        return this;
    }

    public boolean init() {
        if (template == null || template.get() == null) return false;
        else {
            if (PenguinConfig.enableDebuggingTools) {
                BuildingMaterialsCalculator.log(getRegistryName(), this.template.get());
            }

            if (temp_r != null && Building.REGISTRY.get(temp_r) != null) {
                BuildingUpgrades.add(Building.REGISTRY.get(temp_r), this);
                if (overrides) BuildingUpgrades.markOverrides(this);
                temp_r = null;
            }

            //Initialise the NPC Data
            for (Placeable placeable : this.template.get().getComponents()) {
                if (placeable instanceof PlaceableLiving) {
                    ResourceLocation resource = ((PlaceableLiving) placeable).getEntityName();
                    if (resource.equals(NPCS)) {
                        NPC npc = NPC.getNPCFromRegistry(new ResourceLocation(((PlaceableLiving) placeable).getTag().getString("NPC")));
                        if (npc != NPC.NULL_NPC) {
                            this.residents.add(npc);
                            this.waypoints.put(npc.getRegistryName().toString(), placeable.getOffsetPos());
                        }
                    }
                } else if (placeable instanceof PlaceableWaypoint) {
                    this.waypoints.put(((PlaceableWaypoint) placeable).getName(), placeable.getOffsetPos());
                }
            }

            this.template.clear();
            return true;
        }
    }

    public void setOffset(BlockPos offset) {
        offsetX = offset.getX();
        offsetY = offset.getY();
        offsetZ = offset.getZ();
    }

    public void setOffset(int index, int value) {
        if (index == 0) offsetX = value;
        if (index == 1) offsetY = value;
        if (index == 2) offsetZ = value;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Template getTemplate() {
        Template t = template.get();
        if (t == null) {
            t = template.set(GsonHelper.get().fromJson(ResourceLoader.getJSONResource(getRegistryName(), "custom/buildings"), Template.WrappedTemplate.class));
        }

        return t;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public int getLimit() {
        return limit;
    }

    public void setDailyHandler(ResourceLocation dailyScript) {
        this.dailyScript = dailyScript;
    }

    public ResourceLocation getDailyHandler() {
        return dailyScript;
    }

    public Collection<NPC> getResidents() {
        return residents;
    }

    public boolean hasWaypoint(String waypoint) {
        return waypoints.containsKey(waypoint);
    }

    public BlockPos getWaypoint(String waypoint) {
        return waypoints.get(waypoint);
    }
}

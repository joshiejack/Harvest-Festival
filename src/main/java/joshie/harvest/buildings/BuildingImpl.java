package joshie.harvest.buildings;

import com.google.gson.annotations.Expose;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.api.core.ISpecialPurchaseRules;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

import java.util.*;

public class BuildingImpl extends Impl<BuildingImpl> implements Building {
    //Offsets
    private final HashMap<String, PlaceableNPC> npc_offsets = new HashMap<>();
    private final Set<ResourceLocation> inhabitants = new HashSet<>();

    //Costs and rules
    private ISpecialPurchaseRules special = (w, p) -> true;
    private String toLocalise = "";
    private ResourceLocation[] requirements = new ResourceLocation[0];
    private long cost = 1000L;
    private int wood = 64;
    private int stone = 64;
    private int offsetY = -1;
    private long tickTime = 15L;
    private int width;
    private int length;
    private boolean canHaveMultiple;
    private boolean isPurchaseable = true;

    @Expose
    public Placeable[] components;

    public BuildingImpl(){}

    public BuildingImpl setCosts(long cost, int wood, int stone) {
        this.cost = cost;
        this.wood = wood;
        this.stone = stone;
        return this;
    }

    void initBuilding(BuildingImpl building) {
        if (building.components == null) return;
        for (Placeable placeable: building.components) {
            if (placeable instanceof PlaceableNPC) {
                PlaceableNPC npc = ((PlaceableNPC)placeable);
                String home = npc.getHomeString();
                if (home != null) {
                    npc_offsets.put(home, npc);
                }

                //Add this npc as living in the building
                if (npc.getNPC() != null) {
                    inhabitants.add(new ResourceLocation(npc.getNPC()));
                }
            }
        }

        if (this.getRegistryName() != null) {
            this.toLocalise = this.getRegistryName().getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + this.getRegistryName().getResourcePath().toLowerCase(Locale.ENGLISH);
        }
    }

    public Collection<? extends ResourceLocation> getInhabitants() {
        return inhabitants;
    }

    private ResourceLocation getResourceFromName(String resource) {
        if (resource.contains(":")) return new ResourceLocation(resource);
        else return new ResourceLocation("harvestfestival", resource);
    }

    @Override
    public Building setRequirements(String... requirements) {
        this.requirements = new ResourceLocation[requirements.length];
        for (int i = 0; i < requirements.length; i++) {
            this.requirements[i] = getResourceFromName(requirements[i]);
        }

        return this;
    }

    @Override
    public Building setTickTime(long time) {
        this.tickTime = time;
        return this;
    }

    @Override
    public Building setOffset(int width, int offsetY, int length) {
        this.width = width;
        this.offsetY = offsetY;
        this.length = length;
        return this;
    }

    @Override
    public Building setSpecialRules(ISpecialPurchaseRules special) {
        this.special = special;
        return this;
    }

    @Override
    public Building setMultiple() {
        this.canHaveMultiple = true;
        return this;
    }

    @Override
    public Building setNoPurchase() {
        this.isPurchaseable = false;
        return this;
    }

    @Override
    public ISpecialPurchaseRules getRules() {
        return special;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getLocalisedName() {
        return I18n.translateToLocal(toLocalise);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public int getWoodCount() {
        return wood;
    }

    @Override
    public int getStoneCount() {
        return stone;
    }

    @Override
    public ItemStack getBlueprint() {
        return HFBuildings.BLUEPRINTS.getStackFromObject(this);
    }

    @Override
    public ItemStack getSpawner() {
        return HFBuildings.STRUCTURES.getStackFromObject(this);
    }

    @Override
    public EnumActionResult generate(World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        return generate(world, pos, rotation);
    }

    @Override
    public EnumActionResult generate(World world, BlockPos pos, Rotation rotation) {
        if (!world.isRemote) {
            HFBuildings.loadBuilding(this);
            if (components != null) {
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.BUILD, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.DECORATE, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.PAINT, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.MOVEIN, false);
                TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).addBuilding(world, this, rotation, pos);
                MCServerHelper.markForUpdate(world, pos);
            }
        }


        return EnumActionResult.SUCCESS;
    }

    public PlaceableNPC getNPCOffset(String npc_location) {
        return npc_offsets.get(npc_location);
    }

    public long getTickTime() {
        return tickTime;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean hasRequirements(EntityPlayer player) {
        return requirements.length == 0 || TownHelper.getClosestTownToEntity(player).hasBuildings(requirements);
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public boolean canPurchase() {
        return isPurchaseable;
    }

    public boolean canHaveMultiple() {
        return canHaveMultiple;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BuildingImpl && getRegistryName() != null && getRegistryName().equals(((BuildingImpl) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }
}

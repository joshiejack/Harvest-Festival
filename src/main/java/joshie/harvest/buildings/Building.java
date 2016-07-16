package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.shops.ISpecialPurchaseRules;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.blocks.PlaceableDecorative;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import joshie.harvest.core.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Building extends Impl<Building> implements IBuilding {
    //Components
    private transient HashMap<String, PlaceableNPC> npc_offsets = new HashMap<>();
    private transient ArrayList<PlaceableBlock> block_list = new ArrayList<>();
    private transient ArrayList<Placeable> full_list = new ArrayList<>();

    //Costs and rules
    private transient ISpecialPurchaseRules special = (w, p) -> true;
    private transient String toLocalise = "";
    private transient ResourceLocation[] requirements = new ResourceLocation[0];
    private transient long cost = 1000L;
    private transient int wood = 64;
    private transient  int stone = 64;
    private transient int offsetY = -1;
    private transient long tickTime = 20L;

    public Placeable[] components; //Set to null after loading

    public Building(){}

    public Building setCosts(long cost, int wood, int stone) {
        this.cost = cost;
        this.wood = wood;
        this.stone = stone;
        return this;
    }

    public void initBuilding(Building building) {
        full_list = new ArrayList<>();
        Collections.addAll(full_list, building.components);
        for (Placeable placeable: full_list) {
            addToList(placeable);
        }

        if (this.getRegistryName() != null) {
            this.toLocalise = this.getRegistryName().getResourceDomain().toLowerCase() + ".structures." + this.getRegistryName().getResourcePath().toLowerCase();
        }

        this.components = null; //Wipe out my components
     }

    public void addToList(Placeable placeable) {
        if (placeable instanceof PlaceableBlock) {
            PlaceableBlock block = (PlaceableBlock) placeable;
            if (block.getBlock() != Blocks.AIR) {
                if (!(block instanceof PlaceableDecorative)) {
                    block_list.add((PlaceableBlock) placeable);
                }
            }
        }

        if (placeable instanceof PlaceableNPC) {
            PlaceableNPC npc = ((PlaceableNPC)placeable);
            String home = npc.getHomeString();
            if (home != null) {
                npc_offsets.put(home, npc);
            }
        }
    }

    private ResourceLocation getResourceFromName(String resource) {
        if (resource.contains(":")) return new ResourceLocation(resource);
        else return new ResourceLocation("harvestfestival", resource);
    }

    @Override
    public IBuilding setRequirements(String... requirements) {
        this.requirements = new ResourceLocation[requirements.length];
        for (int i = 0; i < requirements.length; i++) {
            this.requirements[i] = getResourceFromName(requirements[i]);
        }

        return this;
    }

    @Override
    public IBuilding setTickTime(long time) {
        this.tickTime = time;
        return this;
    }

    @Override
    public IBuilding setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    @Override
    public IBuilding setSpecialRules(ISpecialPurchaseRules special) {
        this.special = special;
        return this;
    }

    @Override
    public ISpecialPurchaseRules getRules() {
        return special;
    }

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
        if (!world.isRemote && full_list != null) {
            Direction direction = Direction.withMirrorAndRotation(mirror, rotation);
            pos = pos.up(offsetY).up();
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.BUILD);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.PAINT);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.DECORATE);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.MOVEIN);
            TownHelper.getClosestTownToBlockPosOrCreate(world, pos).addBuilding(world, this, direction, pos);
            MCServerHelper.markForUpdate(world, pos);
        } else if (world.isRemote) MCClientHelper.refresh();


        return EnumActionResult.SUCCESS;
    }

    public List<Placeable> getFullList() {
        return full_list;
    }

    public List<PlaceableBlock> getPreviewList() {
        return block_list;
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
        return TownHelper.getClosestTownToPlayer(player).hasBuildings(requirements);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Building && getRegistryName() != null && getRegistryName().equals(((Building) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }
}

package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.buildings.IBuildingProvider;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BuildingProvider implements IBuildingProvider {
    //List of all placeable elements
    private final HashMap<String, PlaceableNPC> npc_offsets = new HashMap<>();
    private final ArrayList<PlaceableBlock> block_list = new ArrayList<>();
    private ArrayList<Placeable> full_list;
    private IBuilding building;

    public BuildingProvider() {}

    public void setBuilding(IBuilding building) {
        this.building = building;
    }

    public void setList(ArrayList<Placeable> list) {
        this.full_list = list;
    }

    public void setList(Placeable[] list) {
        this.full_list = new ArrayList<Placeable>();
        Collections.addAll(full_list, list);
    }

    public void addToList(Placeable placeable) {
        if (placeable instanceof PlaceableBlock) {
            PlaceableBlock block = (PlaceableBlock) placeable;
            if (block.getBlock() != Blocks.AIR) {
                block_list.add((PlaceableBlock) placeable);
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

    @Override
    public IBuilding getBuilding() {
        return building;
    }

    @Override
    public ItemStack getPreview() {
        return HFBlocks.PREVIEW.getItemStack(building);
    }

    @Override
    public PlaceableNPC getNPCOffset(String npc_location) {
        return npc_offsets.get(npc_location);
    }

    @Override
    public int getSize() {
        return full_list.size();
    }

    @Override
    public List<PlaceableBlock> getPreviewList() {
        return block_list;
    }

    @Override
    public List<Placeable> getFullList() {
        return full_list;
    }

    @Override
    public EnumActionResult generate(World world, BlockPos pos) {
        if (!world.isRemote && full_list != null) {
            Direction direction = Direction.values()[world.rand.nextInt(Direction.values().length)];
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.BUILD);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.PAINT);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.DECORATE);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.MOVEIN);
            TownHelper.getClosestTownToBlockPosOrCreate(world.provider.getDimension(), pos).addBuilding(getBuilding(), direction, pos);
        } else if (world.isRemote) {
            MCClientHelper.refresh();
        }

        return EnumActionResult.SUCCESS;
    }
}
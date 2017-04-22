package joshie.harvest.core.util;

import com.google.gson.annotations.Expose;
import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HFTemplate {
    @Expose
    private Placeable[] components;
    private final HashMap<String, PlaceableNPC> npc_offsets = new HashMap<>();

    public HFTemplate() {}
    public HFTemplate(ArrayList<Placeable> ret) {
        components = new Placeable[ret.size()];
        for (int j = 0; j < ret.size(); j++) {
            components[j] = ret.get(j);
        }
    }

    public void merge(HFTemplate park) {
        List<Placeable> set = new ArrayList<>();
        for (Placeable component: park.components) {
            if (!set.contains(component)) set.add(component);
        }

        for (Placeable component: components) {
            if (!set.contains(component)) set.add(component);
        }

        components = set.toArray(new Placeable[set.size()]);
    }

    public void initTemplate() {
        for (Placeable placeable: components) {
            if (placeable instanceof PlaceableNPC) {
                PlaceableNPC npc = ((PlaceableNPC)placeable);
                String home = npc.getHomeString();
                if (home != null) {
                    npc_offsets.put(home, npc);
                }
            }
        }
    }

    public Placeable[] getComponents() {
        return components;
    }

    public void removeBlocks(World world, BlockPos pos, Rotation rotation, IBlockState state) {
        if (!world.isRemote) {
            if (components != null) {
                for (int i = components.length - 1; i >= 0; i--) components[i].remove(world, pos, rotation, ConstructionStage.DECORATE, state);
                for (int i = components.length - 1; i >= 0; i--) components[i].remove(world, pos, rotation, ConstructionStage.BUILD, state);
                MCServerHelper.markForUpdate(world, pos);
            }
        }
    }

    public EnumActionResult placeBlocks(World world, BlockPos pos, Rotation rotation, @Nullable Building building) {
        return placeBlocks(world, pos, rotation, building, Placeable.DEFAULT);
    }

    public EnumActionResult placeBlocks(World world, BlockPos pos, Rotation rotation, @Nullable Building building, @Nullable Replaceable replaceable) {
        if (!world.isRemote) {
            if (components != null) {
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.BUILD, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.DECORATE, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.PAINT, false);
                for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.MOVEIN, false);
                if (building != null) {
                    TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos, true).addBuilding(world, building, rotation, pos);
                }

                MCServerHelper.markForUpdate(world, pos);
            }
        }


        return EnumActionResult.SUCCESS;
    }

    public PlaceableNPC getNPCOffset(String npc_location) {
        return npc_offsets.get(npc_location);
    }

    public static class Replaceable {
        public boolean cantReplace(World world, BlockPos transformed) {
            return world.getBlockState(transformed).getBlockHardness(world, transformed) == -1F;
        }
    }
}

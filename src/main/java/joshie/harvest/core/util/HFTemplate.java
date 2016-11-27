package joshie.harvest.core.util;

import com.google.gson.annotations.Expose;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.helpers.MCServerHelper;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class HFTemplate {
    @Expose
    private Placeable[] components;

    public HFTemplate() {}
    public HFTemplate(ArrayList<Placeable> ret) {
        components = new Placeable[ret.size()];
        for (int j = 0; j < ret.size(); j++) {
            components[j] = ret.get(j);
        }
    }

    public Placeable[] getComponents() {
        return components;
    }

    public EnumActionResult placeBlocks(World world, BlockPos pos, Rotation rotation) {
        if (components != null) {
            for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.BUILD, false);
            for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.DECORATE, false);
            for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.PAINT, false);
            for (Placeable placeable : components) placeable.place(world, pos, rotation, ConstructionStage.MOVEIN, false);
            MCServerHelper.markForUpdate(world, pos);
        }


        return EnumActionResult.SUCCESS;
    }
}

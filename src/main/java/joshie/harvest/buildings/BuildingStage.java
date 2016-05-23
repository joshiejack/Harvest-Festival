package joshie.harvest.buildings;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.town.TownData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    private final EntityNPCBuilder entity;
    public Building building;
    public Direction direction;
    public ConstructionStage stage;
    public int index;
    public BlockPos pos;

    public BuildingStage(EntityNPCBuilder builder) {
        this.entity = builder;
    }

    public BuildingStage(EntityNPCBuilder builder, Building building, BlockPos pos, Mirror mirror, Rotation rotation) {
        this.entity = builder;
        this.building = building;
        this.direction = Direction.withMirrorAndRotation(mirror, rotation);
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.pos = pos.add(0, building.getOffsetY(), 0);
    }

    public BlockPos next() {
        return index < building.getFullList().size() ? building.getFullList().get(index).getTransformedPosition(pos, direction): pos;
    }

    public void build(World world) {
        if (index >= building.getFullList().size()) {
            if (stage == ConstructionStage.BUILD) {
                stage = ConstructionStage.DECORATE;
                index = 0;
            } else if (stage == ConstructionStage.DECORATE) {
                stage = ConstructionStage.PAINT;
                index = 0;
            } else if (stage == ConstructionStage.PAINT) {
                stage = ConstructionStage.MOVEIN;
                index = 0;
            } else if (stage == ConstructionStage.MOVEIN) {
                stage = ConstructionStage.FINISHED;
                index = 0;


                TownData data = TownHelper.getClosestTownToEntityOrCreate(entity);
                data.addBuilding(world, building, direction, pos);
                entity.resetSpawnHome();
            }
        } else {
            while (index < building.getFullList().size()) {
                Placeable block = building.getFullList().get(index);
                if (block.place(world, pos, direction, stage)) {
                    index++;
                    return;
                }

                index++;
            }
        }

        return;
    }

    public long getTickTime() {
        return building.getTickTime();
    }

    public boolean isFinished() {
        return stage == ConstructionStage.FINISHED;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = BuildingRegistry.REGISTRY.getObject(new ResourceLocation(nbt.getString("CurrentlyBuilding")));
        direction = Direction.valueOf(nbt.getString("Direction"));
        pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));

        if (nbt.hasKey("Stage")) {
            index = nbt.getInteger("Index");
            stage = ConstructionStage.values()[nbt.getInteger("Stage")];
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", BuildingRegistry.REGISTRY.getNameForObject(building).toString());
        nbt.setString("Direction", direction.name());
        nbt.setInteger("BuildingX", pos.getX());
        nbt.setInteger("BuildingY", pos.getY());
        nbt.setInteger("BuildingZ", pos.getZ());

        if (stage != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
        }
    }
}
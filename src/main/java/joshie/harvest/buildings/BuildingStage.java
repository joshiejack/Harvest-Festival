package joshie.harvest.buildings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    public UUID owner;
    public IBuilding building;
    public Direction direction;
    public ConstructionStage stage;
    public int index;
    public BlockPos pos;

    public BuildingStage() {}

    public BuildingStage(UUID uuid, IBuilding building, BlockPos pos, Mirror mirror, Rotation rotation) {
        this.owner = uuid;
        this.building = building;
        this.direction = Direction.withMirrorAndRotation(mirror, rotation);
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.pos = pos.add(0, building.getOffsetY(), 0);
    }

    public BlockPos next() {
        return index < building.getProvider().getFullList().size() ? building.getProvider().getFullList().get(index).getTransformedPosition(pos, direction): pos;
    }

    public BlockPos build(World world) {
        if (index >= building.getProvider().getSize()) {
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

                HFTrackers.getPlayerTracker(owner).getTown().addBuilding(world, this);
            }
        } else {
            while (index < building.getProvider().getSize()) {
                Placeable block = building.getProvider().getFullList().get(index);
                if (block.place(owner, world, pos, direction, stage)) {
                    index++;
                    return block.getTransformedPosition(pos, direction);
                }

                index++;
            }
        }

        return pos;
    }

    public long getTickTime() {
        return building.getTickTime();
    }

    public boolean isFinished() {
        return stage == ConstructionStage.FINISHED;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = HFApi.BUILDINGS.getBuildingFromName(new ResourceLocation(nbt.getString("CurrentlyBuilding")));
        direction = Direction.valueOf(nbt.getString("Direction"));
        pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));

        if (nbt.hasKey("Owner-UUIDMost")) {
            index = nbt.getInteger("Index");
            stage = ConstructionStage.values()[nbt.getInteger("Stage")];
            owner = new UUID(nbt.getLong("Owner-UUIDMost"), nbt.getLong("Owner-UUIDLeast"));
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", building.getResource().toString());
        nbt.setString("Direction", direction.name());
        nbt.setInteger("BuildingX", pos.getX());
        nbt.setInteger("BuildingY", pos.getY());
        nbt.setInteger("BuildingZ", pos.getZ());

        if (owner != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
            nbt.setLong("Owner-UUIDMost", owner.getMostSignificantBits());
            nbt.setLong("Owner-UUIDLeast", owner.getLeastSignificantBits());
        }
    }
}
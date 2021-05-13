package uk.joshiejack.settlements.building;

import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.network.building.PacketClearPreview;
import uk.joshiejack.settlements.network.building.PacketDisplayBuildingMessage;
import uk.joshiejack.settlements.network.building.PacketSyncPreview;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;

public class BuildingPlacement {
    private static final Map<UUID, BuildingPlacement> SERVER_PREVIEW = new WeakHashMap<>();
    private static BuildingPlacement CLIENT_PREVIEW = null;
    private final Rotation rotation;
    private final BlockPos pos;
    private final Building building;
    private final EntityPlayer player;
    private final boolean demolish;
    private final BlockPos original;
    private BuildingPlacementMode mode;

    private BuildingPlacement(EntityPlayer player, Building building, BlockPos pos, BlockPos original, Rotation rotation, boolean demolish) {
        this.player = player;
        this.building = building;
        this.pos = pos;
        this.original = original;
        this.rotation = rotation;
        this.demolish = demolish;
        this.mode = BuildingPlacementMode.PREVIEW;
    }

    public static BuildingPlacement getClientPreview(ItemStack held, EntityPlayer player) {
        if (CLIENT_PREVIEW != null) {
            return CLIENT_PREVIEW;
        }

        return getFromPlayer(held, player);
    }

    public BuildingPlacementMode getMode() {
        return mode;
    }

    public Building getBuilding() {
        return building;
    }

    //Called on the server side only
    public static BuildingPlacement onActivated(Building building, EntityPlayer player) {
        UUID team = PenguinTeams.get(player.world).getTeamUUIDForPlayer(player);
        BuildingPlacement placement = SERVER_PREVIEW.get(team);
        if (placement != null && (placement.player.getEntityWorld() != player.getEntityWorld() || placement.player.getHeldItemMainhand() != player.getHeldItemMainhand())) SERVER_PREVIEW.remove(team);
        if (player.world.provider.getDimension() != 0) {
            PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(null, null, BuildingMessage.DIMENSION), player);
            return placement;
        } else {
            if (placement == null || (!placement.demolish && placement.building != building)) { //If the placement was empty or this is a different building, then we need to create a new one
                placement = getFromPlayer(player.getHeldItemMainhand(), player);
                if (placement != null) {
                    Town<?> town = TownFinder.find(player.world, placement.getPosition());
                    if (!placement.demolish && (!player.capabilities.isCreativeMode && town.getLandRegistry().getBuildingCount(building) >= building.getLimit())) {
                        PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(placement.building, placement.pos, BuildingMessage.LIMIT), player);
                    } else {
                        SERVER_PREVIEW.put(team, placement);
                        PenguinNetwork.sendToTeam(new PacketSyncPreview(placement.building, placement.pos, placement.rotation, placement.demolish), player.world, team);
                        PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(placement.building, placement.pos, placement.demolish ? BuildingMessage.DEMOLISH: BuildingMessage.PLACED), player);
                    }
                }
            } else { //Otherwise we already have the preview up and displaying???
                if (placement.player == player) {
                    if (player.isSneaking()) {
                        placement.mode = BuildingPlacementMode.PLACED;
                        PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(placement.building, placement.pos, BuildingMessage.BUILDING), player);
                    } else {
                        PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(placement.building, placement.pos, BuildingMessage.CANCELLED), player);
                    }

                    //Clear out the preview as we've cancelled or confirmed
                    PenguinNetwork.sendToTeam(new PacketClearPreview(), player.world, team);
                    SERVER_PREVIEW.remove(team); //Remove it from here
                } else {
                    PenguinNetwork.sendToClient(new PacketDisplayBuildingMessage(placement.building, placement.pos, BuildingMessage.TEAM_BUSY), player);
                }
            }

            return placement;
        }
    }

    @SuppressWarnings("unchecked, ConstantConditions")
    @Nullable
    private static BuildingPlacement getFromPlayer(ItemStack held, EntityPlayer player) {
        Vec3d vec3d = player.getPositionEyes(0F);
        Vec3d vec3d1 = player.getLook(0F);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * 24, vec3d1.y * 24, vec3d1.z * 24);
        RayTraceResult raytrace = player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
        if (raytrace != null) {
            Building building = held.getItem() instanceof ItemMultiMap ? ((ItemMultiMap<Building>)held.getItem()).getObjectFromStack(held) : Building.NULL;
            boolean upgrade = BuildingUpgrades.isUpgrade(building);
            boolean demolish =  player.getHeldItemMainhand().getItem() == AdventureItems.DESTROY;
            BlockPos original = raytrace.getBlockPos().offset(raytrace.sideHit);
            BlockPos pos = original;
            Rotation rotation = EntityHelper.getRotationFromEntity(player);

            if (demolish || upgrade) {
                Town<?> town = TownFinder.find(player.world, pos);
                if (town == null) return null;
                TownBuilding location = upgrade ? town.getLandRegistry().getBuildingLocation(BuildingUpgrades.getTargetForUpgrade(building)) : town.getLandRegistry().getClosestBuilding(player.world, pos);
                if (location == TownBuilding.NULL) return null;
                else {
                    pos = location.getPosition();
                    rotation = location.getRotation();
                    if (demolish) {
                        building = location.getBuilding();
                    }
                }
            } else {
                pos = pos.up(building.getOffsetY());
                EnumFacing facing = EntityHelper.getFacingFromEntity(player).getOpposite();
                int length = building.getOffsetZ();
                int width = building.getOffsetX();
                switch (facing) {
                    case NORTH:
                        pos = pos.offset(facing, length).offset(EnumFacing.EAST, width);
                        break;
                    case SOUTH:
                        pos = pos.offset(facing, length).offset(EnumFacing.WEST, width);
                        break;
                    case EAST:
                        pos = pos.offset(facing, length).offset(EnumFacing.SOUTH, width);
                        break;
                    case WEST:
                        pos = pos.offset(facing, length).offset(EnumFacing.NORTH, width);
                        break;
                }
            }

            return new BuildingPlacement(player, building, pos, original, rotation, demolish);
        }

        return null;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public BlockPos getOriginalPosition() { return original; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingPlacement placement = (BuildingPlacement) o;
        return demolish == placement.demolish &&
                rotation == placement.rotation &&
                Objects.equals(building, placement.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotation, building, demolish);
    }

    @SideOnly(Side.CLIENT)
    public static void set(Building building, BlockPos pos, Rotation rotation, boolean demolish) {
        CLIENT_PREVIEW = new BuildingPlacement(null, building, pos, pos, rotation, demolish);
    }

    @SideOnly(Side.CLIENT)
    public static void clear() {
        CLIENT_PREVIEW = null; //Clear out the locked preview
    }

    public TownBuilding toTownBuilding() {
        return new TownBuilding(building, pos, rotation);
    }

    public boolean isDemolish() {
        return demolish;
    }

    public enum BuildingPlacementMode {
        PREVIEW, PLACED
    }
}

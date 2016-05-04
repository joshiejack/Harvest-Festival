package joshie.harvest.api.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public interface IBuilding {
    /** Returns the name of this building group **/
    public String getName();

    /** How much gold this building costs **/
    public long getCost();

    /** How much wood this building costs **/
    public int getWoodCount();

    /** How much stone this building costs **/
    public int getStoneCount();

    /** Whether this building can be purchased or not **/
    public boolean canBuy(World world, EntityPlayer player);

    /** Add all the blocks to this building **/
    IBuilding addBlocks();

    List<Placeable> getList();

    int getOffsetY();

    int getSize();

    long getTickTime();

    ItemStack getPreview();

    PlaceableNPC getNPCOffset(String npc_location);

    EnumActionResult generate(UUID playerUUID, World world, BlockPos pos);
}
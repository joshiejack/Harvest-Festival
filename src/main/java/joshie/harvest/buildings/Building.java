package joshie.harvest.buildings;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.shops.ISpecialPurchaseRules;
import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.buildings.placeable.entities.PlaceableNPC;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.items.HFItems;
import net.minecraft.block.state.IBlockState;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Building extends net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl<Building> implements IBuilding {
    //Components
    private transient HashMap<String, PlaceableNPC> npc_offsets = new HashMap<>();
    private transient ArrayList<PlaceableBlock> block_list = new ArrayList<>();
    private transient ArrayList<Placeable> full_list = new ArrayList<>();

    //Costs and rules
    private transient ISpecialPurchaseRules special = new SpecialRulesDefault();
    private transient String toLocalise;
    public ResourceLocation[] requirements;
    public long cost;
    public int wood;
    public int stone;
    public int offsetY;
    public long tickTime;
    public Placeable[] components; //Set to null after loading

    public Building(){}

    @Override
    public String getLocalisedName() {
        return I18n.translateToLocal(toLocalise);
    }

    public void initBuilding() {
        full_list = new ArrayList<>();
        Collections.addAll(full_list, components);
        for (Placeable placeable: full_list) {
            addToList(placeable);
        }

        if (getRegistryName() != null) {
            this.toLocalise = getRegistryName().getResourceDomain().toString().toLowerCase() + ".structures." + getRegistryName().getResourcePath().toLowerCase();
        }

        components = null;
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

    public List<Placeable> getFullList() {
        return full_list;
    }

    public PlaceableNPC getNPCOffset(String npc_location) {
        return npc_offsets.get(npc_location);
    }

    public List<PlaceableBlock> getPreviewList() {
        return block_list;
    }

    public long getTickTime() {
        return tickTime;
    }

    public int getOffsetY() {
        return offsetY;
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

    public boolean hasRequirements(EntityPlayer player) {
        return TownHelper.getClosestTownToPlayer(player).hasBuildings(requirements);
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
        return HFBlocks.PREVIEW.getItemStack(this);
    }

    @Override
    public ItemStack getSpawner() {
        return HFItems.STRUCTURES.getStackFromObject(this);
    }

    @Override
    public EnumActionResult generate(World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        if (!world.isRemote && full_list != null) {
            Direction direction = Direction.withMirrorAndRotation(mirror, rotation);
            pos = pos.up(offsetY);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.BUILD);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.PAINT);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.DECORATE);
            for (Placeable placeable: full_list) placeable.place(world, pos, direction, ConstructionStage.MOVEIN);
            TownHelper.getClosestTownToBlockPosOrCreate(world.provider.getDimension(), pos).addBuilding(this, direction, pos);
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 2);
        } else if (world.isRemote) MCClientHelper.refresh();


        return EnumActionResult.SUCCESS;
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

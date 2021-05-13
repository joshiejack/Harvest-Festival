package uk.joshiejack.penguinlib.data.adapters;

import net.minecraft.client.renderer.RenderHelper;
import uk.joshiejack.penguinlib.item.tools.Coordinates;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.PlaceableHelper;
import uk.joshiejack.penguinlib.template.Template;
import uk.joshiejack.penguinlib.template.blocks.*;
import uk.joshiejack.penguinlib.template.entities.PlaceableEntity;
import uk.joshiejack.penguinlib.item.interfaces.PenguinTool;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import uk.joshiejack.penguinlib.util.helpers.generic.GsonHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ChatHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static uk.joshiejack.penguinlib.item.tools.Coordinates.from;
import static uk.joshiejack.penguinlib.item.tools.Coordinates.to;

public class CodeGeneratorBuildings implements PenguinTool {
    public static Template template;
    public static boolean DIFFERENCE;

    private boolean parkLocations;

    @Override
    public void activate(EntityPlayer player, World world, BlockPos pos) {
        if (!Coordinates.areSet) {
            ChatHelper.displayChat("You have not set the coordinates with the coordinates tool!");
        } else getCode(world);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Entity> getEntities(World world, Class clazz, BlockPos position) {
        return (ArrayList<Entity>) world.getEntitiesWithinAABB(clazz, new AxisAlignedBB(position));
    }

    private void buildList(World world, ArrayList<Placeable> ret, BlockPos position, BlockPos offset, Set<Entity> all) {
        Set<Entity> entityList = new HashSet<>();
        entityList.addAll(getEntities(world, EntityPainting.class, position));
        entityList.addAll(getEntities(world, EntityItemFrame.class, position));
        entityList.addAll(getEntities(world, EntityLiving.class, position));
        IBlockState state = world.getBlockState(position);
        Block block = state.getBlock();
        if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST) {
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(position);
            if (chest != null && chest.hasCustomName()) {
                if (!parkLocations) {
                    ret.add(new PlaceableChest(chest.getName(), state, offset));
                    return;
                }
                RenderHelper.enableStandardItemLighting();
            }
        } else if (block == Blocks.FURNACE) {
            TileEntityFurnace furnace = (TileEntityFurnace) world.getTileEntity(position);
            if (furnace != null && furnace.hasCustomName()) {
                ret.add(new PlaceableWaypoint(furnace.getName(), offset));
                return;
            }
        } else if (block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER) {
            TileEntityBanner banner = (TileEntityBanner) world.getTileEntity(position);
            if (banner != null) {
                ret.add(new PlaceableBanner(banner.getItem(), state, offset));
                return;
            }
        } else if (world.getTileEntity(position) instanceof Rotatable) {
            ret.add(new PlaceableRotatable((((Rotatable) world.getTileEntity(position))).getFacing(), state, offset));
            return;
        } else if (world.getTileEntity(position) instanceof TileEntityBed) {
            ret.add(new PlaceableBed(((TileEntityBed)(world.getTileEntity(position))).getColor(), state, offset));
            return;
        }

        if (!parkLocations) {
            if ((block != Blocks.AIR || entityList.size() > 0) && block != Blocks.END_STONE) {
                int meta = state.getBlock().getMetaFromState(state);
                if ((block == Blocks.DOUBLE_PLANT || block instanceof BlockDoor) && meta >= 8) return;
                TileEntity tile = world.getTileEntity(position);
                if (tile instanceof TileEntitySign) {
                    ITextComponent[] text = ((TileEntitySign) tile).signText;
                    if (block == Blocks.STANDING_SIGN) {
                        ret.add(PlaceableHelper.getFloorSignString(text, state, offset));
                    } else ret.add(PlaceableHelper.getWallSignString(text, state, offset));
                } else {
                    Placeable text = PlaceableHelper.getPlaceableBlockString(world, state, offset);
                    ret.add(text);
                }

                //Entities
                if (entityList.size() > 0) {
                    entityList.stream().filter(e -> !all.contains(e)).forEach(e -> {
                        ret.add(PlaceableHelper.getPlaceableEntityString(e, offset));
                        all.add(e);
                    });
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getCode(World world) {
        if (!world.isRemote) {
            ArrayList<Placeable> ret = new ArrayList<>();
            Set<Entity> all = new HashSet();
            BlockPos origin = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
            for (BlockPos position: BlockPos.getAllInBox(from, to)) {
                BlockPos offset = position.add(-origin.getX(), -origin.getY(), -origin.getZ());
                buildList(world, ret, position, offset, all);
            }

            if (DIFFERENCE && template != null) {
                ret.removeIf(p -> {
                    for (Placeable p2: template.getComponents()) {
                        if (p instanceof PlaceableBlock && p2 instanceof PlaceableBlock) {
                            PlaceableBlock r1 = (PlaceableBlock) p;
                            PlaceableBlock r2 = (PlaceableBlock) p2;
                            if (r1.getOffsetPos().equals(r2.getOffsetPos()) && r1.getState() == r2.getState()) return true;
                        }

                        if (p instanceof PlaceableEntity && p2 instanceof PlaceableEntity) {
                            PlaceableEntity r1 = (PlaceableEntity) p;
                            PlaceableEntity r2 = (PlaceableEntity) p2;
                            if (r1.getOffsetPos().equals(r2.getOffsetPos()) && r1.getEntityClass() == r2.getEntityClass()) return true;
                        }
                        //if (p.getOffsetPos().equals(p2.getOffsetPos())) return true;
                    }

                    return false;
                });
            }

            template = new Template(ret);
            try {
                String json = GsonHelper.get().toJson(template);
                PrintWriter writer = new PrintWriter("building.json", "UTF-8");
                writer.write(json);
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (world.isRemote) {
            ChatHelper.displayChat("Generated Building Code");
        }
    }
}
package uk.joshiejack.settlements.entity.ai.action.tasks;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.settlements.network.town.land.PacketRemoveBuilding;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.blocks.PlaceableBlock;
import uk.joshiejack.penguinlib.template.entities.PlaceableEntity;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.List;

@PenguinLoader("destroy")
public class ActionDestroy extends ActionPhysical {
    private TownBuilding location;
    private Placeable.ConstructionStage stage;
    private int index;
    private int timeout;
    private boolean setAnimation;
    private boolean ySet;
    private int y;

    public ActionDestroy() { this.setMemorable(); }
    public ActionDestroy(TownBuilding location) {
        this.location = location;
        this.stage = Placeable.ConstructionStage.FINISHED;
        this.index = location.getBuilding().getTemplate().getComponents().length - 1;
        this.setMemorable();
    }

    private int getYTarget(EntityNPC npc) {
        if (!ySet) {
            y = npc.getEntityWorld().getTopSolidOrLiquidBlock(location.getPosition()).getY();
            ySet = true;
        } else if (timeout == 2500) ySet = false;

        return y;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (location == null) return EnumActionResult.FAIL;
        //Set the animation for the builder
        if (!setAnimation) {
            setAnimation = true;
            npc.setAnimation("build");
        }

            if (npc.getHeldItemMainhand().getItem() != Item.getItemFromBlock(Blocks.TNT)) {
                npc.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Blocks.TNT));
            }

            //Swing the hammer and look at the building
            npc.swingArm(EnumHand.MAIN_HAND);
            int explosions = 0;
            while (true) {
                Placeable current = location.getBuilding().getTemplate().getComponents()[index];
                timeout++; //Timeout limit
                int yTarget = getYTarget(npc);
                //if (timeout >= 5000) {
                    npc.setPositionAndUpdate(location.getPosition().getX() + 0.5, yTarget, location.getPosition().getZ() + 0.5);
                    timeout = 0;
               // }

                if (npc.getDistance(location.getPosition().getX() + 0.5, yTarget, location.getPosition().getZ() + 0.5) < 1 || timeout >= 10000) {
                   // boolean placed = current.place(npc.world, location.getPosition(), location.getRotation(), stage, true);
                    BlockPos lookTarget = BlockPosHelper.getTransformedPosition(current.getOffsetPos(), location.getPosition(), location.getRotation());
                    IBlockState lookState = npc.world.getBlockState(lookTarget);
                    if (current instanceof PlaceableBlock) {
                        npc.world.setBlockToAir(lookTarget); //DESTROYYYYYYYYYYYYYYYYYYY
                    } else if (current instanceof PlaceableEntity) {
                        List<Entity> entity = EntityHelper.getEntities(((PlaceableEntity)current).getEntityClass(), npc.world, lookTarget, 1F, 1F);
                        entity.forEach(Entity::setDead); //Kill all them bitches if they are in place

                    }

                    if (explosions < 24 && npc.world.rand.nextInt(16) == 0) {
                        npc.world.createExplosion(null, lookTarget.getX(), lookTarget.getY(), lookTarget.getZ(), 0F, false);
                        explosions++;
                    }

                    npc.getLookHelper().setLookPosition(lookTarget.getX(), lookTarget.getY(), lookTarget.getZ(), (float)npc.getHorizontalFaceSpeed(), (float)npc.getVerticalFaceSpeed());
                    index--;
                    timeout = 0;

                    if (index == -1) {
                        index = location.getBuilding().getTemplate().getComponents().length - 1; //Reset this
                        stage = stage.previous();
                        if (stage == Placeable.ConstructionStage.FINISHED) {
                            Town<?> town = AdventureDataLoader.get(npc.world).getTownByID(npc.world.provider.getDimension(), npc.getTown());
                            town.getLandRegistry().removeBuilding(npc.world, location);
                            PenguinNetwork.sendToEveryone(new PacketRemoveBuilding(npc.world.provider.getDimension(), town.getID(), location));
                            return EnumActionResult.SUCCESS;
                        }
                    }
                }
            }
        //}

        //return EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (location != null) {
            tag.setString("Building", location.getBuilding().getRegistryName().toString());
            tag.setLong("Pos", location.getPosition().toLong());
            tag.setByte("Rotation", (byte) location.getRotation().ordinal());
            tag.setBoolean("Built", location.isBuilt());
            tag.setByte("Stage", (byte) stage.ordinal());
            tag.setInteger("Index", index);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Building building = Building.REGISTRY.get(new ResourceLocation(nbt.getString("Building")));
        BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
        Rotation rotation = Rotation.values()[nbt.getByte("Rotation")];
        TownBuilding townBuilding = new TownBuilding(building, pos, rotation);
        if (nbt.getBoolean("Built")) townBuilding.setBuilt();
        stage = Placeable.ConstructionStage.values()[nbt.getByte("Stage")];
        index = nbt.getInteger("Index");
    }
}

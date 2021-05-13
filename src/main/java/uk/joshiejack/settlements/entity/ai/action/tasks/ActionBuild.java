package uk.joshiejack.settlements.entity.ai.action.tasks;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.settlements.network.town.land.PacketAddBuilding;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.entities.PlaceableLiving;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.BlockPosHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

@PenguinLoader("build")
public class ActionBuild extends ActionPhysical {
    private Building building;
    private Placeable.ConstructionStage stage;
    private int index;
    private Rotation rotation;
    private BlockPos target;
    private boolean setAnimation;

    public ActionBuild() { this.setMemorable(); }
    public ActionBuild(Building building, BlockPos target, Rotation rotation) {
        this.building = building;
        this.stage = Placeable.ConstructionStage.BUILD;
        this.index = 0;
        this.rotation = rotation;
        this.target = target;
        this.setMemorable();
    }

    @Override
    public Action withData(Object... params) {
        this.building = Building.REGISTRY.get(new ResourceLocation((String) params[0]));
        this.stage = Placeable.ConstructionStage.BUILD;
        this.index = 0;
        this.target = (BlockPos) params[1];
        this.rotation = (Rotation) params[2];
        this.setMemorable();
        return this;
    }

    @Override
    public boolean has(String... data) {
        Building check = Building.REGISTRY.get(new ResourceLocation(data[0]));
        return check != null && check == building;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (building == null) return EnumActionResult.FAIL; //Skip if the building has been removed
        //Set the animation for the builder
        if (!setAnimation) {
            setAnimation = true;
            npc.setAnimation("build");
        }

        if (npc.world.getTotalWorldTime() %10 == 0) {
            //Set the held item to hammer if we are building
            if (npc.getHeldItemMainhand().isEmpty()) {
                npc.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.FLINT)); //TODO: Add a different item that's in vanilla
            }

            while (true) {
                Placeable current = building.getTemplate().getComponents()[index];
                boolean placed = true;
                npc.swingArm(EnumHand.MAIN_HAND);
                if (current instanceof PlaceableLiving && ((PlaceableLiving) current).getEntityName().equals(Building.NPCS)) {
                    ResourceLocation toSpawn = new ResourceLocation(((PlaceableLiving) current).getTag().getString("NPC"));
                    TownServer town = AdventureDataLoader.get(npc.world).getTownByID(npc.world.provider.getDimension(), npc.getTown());
                    town.getCensus().invite(toSpawn); //Spawn the npc in on the next day
                } else placed = current.place(npc.world, target, rotation, stage, true);

                BlockPos lookTarget = BlockPosHelper.getTransformedPosition(current.getOffsetPos(), target, rotation);
                npc.getLookHelper().setLookPosition(lookTarget.getX(), lookTarget.getY(), lookTarget.getZ(), (float)npc.getHorizontalFaceSpeed(), (float)npc.getVerticalFaceSpeed());
                index++;

                if (index >= building.getTemplate().getComponents().length) {
                    index = 0; //Reset this
                    stage = stage.next();
                    if (stage == Placeable.ConstructionStage.FINISHED) {
                        TownBuilding townBuilding = new TownBuilding(building, target, rotation).setBuilt();
                        Town<?> town = AdventureDataLoader.get(npc.world).getTownByID(npc.world.provider.getDimension(), npc.getTown());
                        town.getLandRegistry().addBuilding(npc.world, townBuilding);
                        PenguinNetwork.sendToEveryone(new PacketAddBuilding(npc.world.provider.getDimension(), town.getID(), townBuilding));
                        return EnumActionResult.SUCCESS;
                    }
                }

                if (placed) {
                    break;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Building", building.getRegistryName().toString());
        tag.setByte("Stage", (byte) stage.ordinal());
        tag.setInteger("Index", index);
        tag.setLong("Target", target.toLong());
        tag.setByte("Rotation", (byte) rotation.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        building = Building.REGISTRY.get(new ResourceLocation(nbt.getString("Building")));
        stage = Placeable.ConstructionStage.values()[nbt.getByte("Stage")];
        index = nbt.getInteger("Index");
        target = BlockPos.fromLong(nbt.getLong("Target"));
        rotation = Rotation.values()[nbt.getByte("Rotation")];
    }
}

package uk.joshiejack.horticulture.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.network.PacketStumpStage;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("stump")
public class TileStump extends TileInventory {
    public static final HolderRegistry<MushroomData> registry = new HolderRegistry<>(MushroomData.NULL);
    public IBlockState mushroom = Blocks.AIR.getDefaultState();
    private int stage = 0;
    private int harvests = 0;

    public TileStump() {
        super(1);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        if (stage == 4 && handler.getStackInSlot(0).hasTagCompound()) { //We can pull out only when the mushroom has fully grown
            ItemStack internal = new ItemStack(handler.getStackInSlot(0).getTagCompound());
            MushroomData data = registry.getValue(internal);ItemHandlerHelper.giveItemToPlayer(player, data.stack.copy()); //Copy this item


            harvests++;
            if (harvests >= data.maxHarvests) {
                stage = 0; //YES!
                handler.setStackInSlot(0, ItemStack.EMPTY);
            } else stage = 1;

            onStageChanged();
            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            return true;
        } else if (isStackValidForInsertion(0, player.getHeldItem(hand))) {
            ItemStack stack = player.getHeldItem(hand).splitStack(1);
            handler.setStackInSlot(0, stack);
            stage = 1; //Start off from the beginning just in case
            harvests = 0; //Reset the harvests
            onStageChanged();
            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            return true;
        }

        return false;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        if (stack.getItem() == HorticultureItems.SPORES) {
            if (!stack.hasTagCompound()) return false;
            ItemStack internal = new ItemStack(stack.getTagCompound());
            return registry.getValue(internal) != MushroomData.NULL;
        } else return false;
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return stage == 4; //Can only be remove if the mushroom has grown
    }

    @Nonnull
    public IBlockState getMushroom() {
        return mushroom;
    }

    public int getStage() {
        return stage;
    }

    @Override
    public void updateTick() {
        if (mushroom != Blocks.AIR.getDefaultState() && stage < 4) {
            stage++; //Yes baby that is how we roll, we get this stage right up!
            onStageChanged();
        }
    }

    @Override
    public void dropInventory() {
        if (harvests == 0) {
            super.dropInventory();
        }
    }

    @SideOnly(Side.CLIENT)
    public void setStage(int stage) {
        this.stage = stage; //OMG WE HAVE TO RENDER V
        onStageChanged();
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateMushroomState() {
        ItemStack inside = handler.getStackInSlot(0);
        if (inside.hasTagCompound()) {
            ItemStack internal = new ItemStack(handler.getStackInSlot(0).getTagCompound());
            mushroom = registry.getValue(internal).state;
        } else mushroom = Blocks.AIR.getDefaultState();
    }

    private void onStageChanged() {
        if (!world.isRemote) {
            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            PenguinNetwork.sendToNearby(this, new PacketStumpStage(pos, stage));
        }

        updateMushroomState();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stage = nbt.getInteger("Stage");
        if (stage > 4) {
            stage = 4; //Reset
        }

        updateMushroomState(); //Init
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("Stage", (byte) stage);
        return super.writeToNBT(nbt);
    }

    public static class MushroomData {
        public static final MushroomData NULL = new MushroomData(ItemStack.EMPTY, Blocks.AIR.getDefaultState(), 1, 0);

        private final IBlockState state;
        private final ItemStack stack;
        private final int maxHarvests;
        private final int color;

        public MushroomData(ItemStack stack, IBlockState state, int maxHarvests, int color) {
            this.state = state;
            this.stack = stack;
            this.maxHarvests = maxHarvests;
            this.color = color;
        }

        public boolean init() {
            return true;
        }

        public int getColor() {
            return color;
        }

        public ItemStack getResult() {
            return stack;
        }
    }
}

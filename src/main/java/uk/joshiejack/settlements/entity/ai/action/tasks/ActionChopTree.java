package uk.joshiejack.settlements.entity.ai.action.tasks;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionPhysical;
import uk.joshiejack.penguinlib.item.util.TreeTasks;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

@PenguinLoader("chop_tree")
public class ActionChopTree extends ActionPhysical {
    private int timeTaken;
    private BlockPos tree;
    public ActionChopTree() {}

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (npc.world.getWorldTime() %20 == 0) {
            //If we haven't found a tree, look for one nearby
            if (tree == null) {
                for (int i = 0; i < 256; i++) {
                    Vec3d target = RandomPositionGenerator.findRandomTarget(npc, 12, 8);
                    if (target != null && TreeTasks.findTree(npc.world, new BlockPos(target))
                            && !npc.world.getBlockState(new BlockPos(target).down()).getBlock().isWood(npc.world, new BlockPos(target).down())) {
                        tree = new BlockPos(target);
                        npc.getNavigator().tryMoveToXYZ(target.x, target.y, target.z, 0.5F);
                        break;
                    }
                }
            } else {
                npc.getNavigator().tryMoveToXYZ(tree.getX(), tree.getY(), tree.getZ(), 0.5F);
                int y = tree.getY();
                while (npc.world.getBlockState(new BlockPos(tree.getX(), y, tree.getZ())).getBlock().isWood(npc.world, new BlockPos(tree.getX(), y, tree.getZ()))) {
                    y--;
                }

                if (tree.getDistance((int) npc.posX, y + 1, (int)npc.posZ) < 2) {
                    npc.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
                    npc.swingArm(EnumHand.MAIN_HAND);
                    MinecraftForge.EVENT_BUS.register(new TreeTasks.ChopTree(tree,
                            FakePlayerHelper.getFakePlayerWithPosition((WorldServer) npc.world, tree), npc.getHeldItemMainhand()));
                    tree = null;
                    return EnumActionResult.SUCCESS;
                }

                if (timeTaken > 500) { //time_unit > half_hour
                    tree = null; //Reset if taking too long
                }

                timeTaken++;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}
}

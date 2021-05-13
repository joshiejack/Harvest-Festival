package uk.joshiejack.penguinlib.item;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.adapters.CodeGeneratorBuildings;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import uk.joshiejack.penguinlib.item.interfaces.PenguinTool;
import uk.joshiejack.penguinlib.item.tools.AirPlacer;
import uk.joshiejack.penguinlib.item.tools.Coordinates;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ChatHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class ItemTools extends ItemMulti<ItemTools.Tool> {
    public ItemTools() {
        super(new ResourceLocation(MOD_ID, "tools"), Tool.class);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        getEnumFromStack(player.getHeldItem(hand)).tool.activate(player, world, pos);
        return EnumActionResult.SUCCESS; //Everytime
    }

    public enum Tool {
        COORDINATES(new Coordinates()),
        BUILDING(new CodeGeneratorBuildings()),
        AIR_PLACER(new AirPlacer(false)),
        BLOCK_STATE((player, world, pos) -> PenguinLib.logger.log(Level.INFO, world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)))),
        NEW_DAY((player, world, position) -> world.setWorldTime((TimeHelper.getElapsedDays(world) + 1) * TimeHelper.TICKS_PER_DAY)),
        AIR_REMOVER(new AirPlacer(true)),
        GREEN_SCREEN((p, w, b) -> p.openGui(PenguinLib.instance, 1, w, 0, 0, 0)),
        DIFFERENCE(((player, world, pos) ->  {
            if (!world.isRemote) CodeGeneratorBuildings.DIFFERENCE = !CodeGeneratorBuildings.DIFFERENCE;
            if (world.isRemote) {
                ChatHelper.displayChat(CodeGeneratorBuildings.DIFFERENCE ? "Difference Disabled!" : "Difference Enabled!");
            }
        }));

        private PenguinTool tool;

        Tool(PenguinTool tool) {
            this.tool = tool;
        }
    }
}

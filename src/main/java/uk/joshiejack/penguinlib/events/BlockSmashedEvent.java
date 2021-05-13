package uk.joshiejack.penguinlib.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class BlockSmashedEvent extends PlayerEvent {
    private final IBlockState state;
    private final BlockPos pos;
    private final EnumHand hand;

    public BlockSmashedEvent(EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state) {
        super(player);
        this.state = state;
        this.pos = pos;
        this.hand = hand;
    }

    public IBlockState getState() {
        return state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumHand getHand() {
        return hand;
    }
}

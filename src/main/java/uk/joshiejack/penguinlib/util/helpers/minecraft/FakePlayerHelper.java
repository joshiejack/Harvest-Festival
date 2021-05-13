package uk.joshiejack.penguinlib.util.helpers.minecraft;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class FakePlayerHelper {
    private static final GameProfile PENGUIN_PLAYER = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-CA57-13C2C99CAE63"), "[PenguinLib]");
    private static WeakReference<FakePlayer> PLAYER = null;

    public static FakePlayer getFakePlayerWithPosition(WorldServer world, BlockPos pos) {
        FakePlayer ret = PLAYER != null ? PLAYER.get() : null;
        if (ret == null) {
            ret = FakePlayerFactory.get(world, PENGUIN_PLAYER);
            PLAYER = new WeakReference<>(ret);
        }

        //Set the position
        ret.posX = pos.getX();
        ret.posY = pos.getY();
        ret.posZ = pos.getZ();
        return ret;
    }
}

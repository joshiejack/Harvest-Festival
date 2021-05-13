package uk.joshiejack.seasons.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.world.weather.Weather;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncWorldData extends PenguinPacket {
    private Weather weather;
    private Season season;

    public PacketSyncWorldData() {}
    public PacketSyncWorldData(Weather weather, Season season) {
        this.weather = weather;
        this.season = season;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(weather.ordinal());
        to.writeByte(season.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        weather = Weather.values()[from.readByte()];
        season = Season.values()[from.readByte()];
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        WorldDataClient.INSTANCE.setWeather(weather);
        WorldDataClient.INSTANCE.setSeason(season);
        player.world.provider.calculateInitialWeather();
    }
}

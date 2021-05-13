package uk.joshiejack.seasons.world.storage;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.ArrayHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.network.PacketSyncWorldData;
import uk.joshiejack.seasons.world.weather.Weather;
import uk.joshiejack.seasons.world.weather.WeatherRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import static uk.joshiejack.seasons.world.weather.Weather.CLEAR;

public class WorldDataServer extends AbstractWorldData implements INBTSerializable<NBTTagCompound> {
    private Weather forecast = CLEAR;
    private boolean generated = false;

    public void onNewDay(World world) {
        recalculate(world, false); //Time has changed!
        if (!generated) { //If we haven't generated any weather forecast yet, then do so!
            generated = true;
        }

        weather = forecast; //Make todays weather the forecasted weather
        forecast = WeatherRegistry.getRandomWeatherForSeason(season, world.rand);
        world.provider.calculateInitialWeather();
        SeasonsSavedData.instance.markDirty();
        PenguinNetwork.sendToEveryone(new PacketSyncWorldData(weather, season));
    }

    public Weather getForecast() {
        return forecast;
    }

    public void changeWeather(Weather weather, boolean today) {
        if (today) this.weather = weather;
        else forecast = weather;
        SeasonsSavedData.instance.markDirty();
        PenguinNetwork.sendToEveryone(new PacketSyncWorldData(weather, season));
    }

    public void recalculate(World world, boolean packet) {
        season = Season.fromTime(world.getWorldTime());
        Seasons.SEASON = season; //Update the local
        SeasonsSavedData.instance.markDirty();
        if (packet) {
            PenguinNetwork.sendToEveryone(new PacketSyncWorldData(weather, season));
        }
    }

    public void synchronize(EntityPlayer player) {
        PenguinNetwork.sendToClient(new PacketSyncWorldData(weather, season), player);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("Season", (byte) season.ordinal());
        compound.setByte("Weather", (byte) weather.ordinal());
        compound.setBoolean("Generated", generated);
        compound.setByte("Forecast", (byte) forecast.ordinal());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        season = ArrayHelper.getArrayValue(Season.values(), nbt.getByte("Season"));
        Seasons.SEASON = season;
        weather = ArrayHelper.getArrayValue(Weather.values(), nbt.getByte("Weather"));
        generated = nbt.getBoolean("Generated");
        forecast = Weather.values()[nbt.getByte("Forecast")];
    }
}

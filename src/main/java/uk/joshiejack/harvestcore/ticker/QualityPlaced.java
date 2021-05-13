package uk.joshiejack.harvestcore.ticker;

import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import javax.annotation.Nullable;

@PenguinLoader("quality")
public class QualityPlaced extends DailyTicker implements HasQuality, INBTSerializable<NBTTagCompound> {
    private Quality quality = null;

    public QualityPlaced(String type) {
        super(type);
    }
    public QualityPlaced() {
        super("quality");
    }

    @Override
    public DailyTicker newInstance() {
        return new QualityPlaced(getType());
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    @Nullable
    @Override
    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (quality != null) {
            tag.setString("Quality", quality.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        if (tag.hasKey("Quality")) {
            quality = Quality.REGISTRY.get(new ResourceLocation(tag.getString("Quality")));
        }
    }
}

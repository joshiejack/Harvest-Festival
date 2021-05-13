package uk.joshiejack.settlements.world.town.people;

import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class Government implements INBTSerializable<NBTTagCompound> {
    private final EnumSet<Ordinance> laws = EnumSet.noneOf(Ordinance.class);
    private final Set<UUID> applications = Sets.newHashSet();
    private Citizenship citizenship = Citizenship.APPLICATION;

    public boolean hasLaw(Ordinance law) {
        return laws.contains(law);
    }

    public void addApplication(UUID uuid) {
        applications.add(uuid);
    }

    public Set<UUID> getApplications() {
        return applications;
    }

    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void toggleCitizenship() {
        citizenship = citizenship.next();
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        NBTTagCompound laws = tag.getCompoundTag("Laws");
        for (Ordinance ordinance : Ordinance.values()) {
            if (laws.getBoolean(ordinance.name())) this.laws.add(ordinance);
            if (!laws.hasKey(ordinance.name()) && ordinance.isEnabledByDefault()) {
                this.laws.add(ordinance);
            }
        }

        if (tag.hasKey("Applications")) {
            NBTTagList apps = tag.getTagList("Applications", 8);
            for (int i = 0; i < apps.tagCount(); i++) {
                applications.add(UUID.fromString(apps.getStringTagAt(i)));
            }
        }

        citizenship = tag.hasKey("Citizenship") ? Citizenship.valueOf(tag.getString("Citizenship")) : Citizenship.OPEN;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound laws = new NBTTagCompound();
        for (Ordinance ordinance : Ordinance.values()) {
            laws.setBoolean(ordinance.name(), this.laws.contains(ordinance));
        }

        tag.setTag("Laws", laws);

        if (applications.size() > 0) {
            NBTTagList apps = new NBTTagList();
            applications.forEach(uuid -> apps.appendTag(new NBTTagString(uuid.toString())));
            tag.setTag("Applications", apps);
        }

        tag.setString("Citizenship", citizenship.name());

        return tag;
    }

    public void setLaw(Ordinance ordinance, boolean enact) {
        if (enact) laws.add(ordinance);
        else laws.remove(ordinance);
    }
}

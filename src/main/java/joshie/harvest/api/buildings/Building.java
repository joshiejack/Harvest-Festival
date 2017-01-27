package joshie.harvest.api.buildings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.render.BuildingKey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Building extends IForgeRegistryEntry.Impl<Building> {
    public static final IForgeRegistry<Building> REGISTRY = new RegistryBuilder<Building>().setName(new ResourceLocation("harvestfestival", "buildings")).setType(Building.class).setIDRange(0, 32000).create();
    //Offsets
    private final Set<ResourceLocation> inhabitants = new HashSet<>();

    //Costs and rules
    private ISpecialRules special = (w, p, a) -> true;
    private String toLocalise = "";
    private ResourceLocation[] requirements = new ResourceLocation[0];
    private int offsetY = -1;
    private long tickTime = 15L;
    private int width;
    private int length;
    private boolean canHaveMultiple;

    public Building() {}
    public Building(ResourceLocation resource){
        this.setRegistryName(resource);
        REGISTRY.register(this);
    }

    public void initBuilding() {
        toLocalise = getRegistryName().getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + this.getRegistryName().getResourcePath().toLowerCase(Locale.ENGLISH);
    }

    public Collection<? extends ResourceLocation> getInhabitants() {
        return inhabitants;
    }

    private ResourceLocation getResourceFromName(String resource) {
        if (resource.contains(":")) return new ResourceLocation(resource);
        else return new ResourceLocation("harvestfestival", resource);
    }

    public Building setRequirements(String... requirements) {
        this.requirements = new ResourceLocation[requirements.length];
        for (int i = 0; i < requirements.length; i++) {
            this.requirements[i] = getResourceFromName(requirements[i]);
        }

        return this;
    }

    public Building setTickTime(long time) {
        this.tickTime = time;
        return this;
    }

    public Building setOffset(int width, int offsetY, int length) {
        this.width = width;
        this.offsetY = offsetY;
        this.length = length;
        return this;
    }

    public Building setInhabitants(NPC... npcs) {
        for (NPC npc: npcs) {
            inhabitants.add(npc.getRegistryName());
        }

        return this;
    }

    public Building setSpecialRules(ISpecialRules special) {
        this.special = special;
        return this;
    }

    @SuppressWarnings("unused")
    public Building setMultiple() {
        this.canHaveMultiple = true;
        return this;
    }

    public ISpecialRules getRules() {
        return special;
    }

    @SuppressWarnings("deprecation")
    public String getLocalisedName() {
        if (StringUtils.isNullOrEmpty(toLocalise)) {
            this.toLocalise = this.getRegistryName().getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + this.getRegistryName().getResourcePath().toLowerCase(Locale.ENGLISH);
        }

        return I18n.translateToLocal(toLocalise);
    }

    @SuppressWarnings("unused")
    public boolean isSuitableLocation(BuildingKey key) {
        return true;
    }

    public ItemStack getBlueprint() {
        return HFApi.buildings.getBlueprint(this);
    }

    public ItemStack getSpawner() {
        return HFApi.buildings.getSpawner(this);
    }

    public long getTickTime() {
        return tickTime;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public boolean hasRequirements(EntityPlayer player) {
        return requirements.length == 0 || HFApi.town.doesClosestTownHaveBuildings(player, requirements);
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    //Called when the day passed over
    public void newDay(World world, BlockPos pos, Rotation rotation, CalendarDate today, CalendarDate yesterday) {}

    //Called when this building has finished being built
    public void onBuilt(World world, BlockPos pos, Rotation rotation) {}

    public boolean canHaveMultiple() {
        return canHaveMultiple;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Building && getRegistryName() != null && getRegistryName().equals(((Building) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }


}

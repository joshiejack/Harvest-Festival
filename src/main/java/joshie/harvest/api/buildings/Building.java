package joshie.harvest.api.buildings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.buildings.render.BuildingKey;
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

import javax.annotation.Nonnull;
import java.util.*;

public class Building extends IForgeRegistryEntry.Impl<Building> {
    public static final IForgeRegistry<Building> REGISTRY = new RegistryBuilder<Building>().setName(new ResourceLocation("harvestfestival", "buildings")).setType(Building.class).setIDRange(0, 32000).create();
    private final Set<NPC> inhabitants = new HashSet<>();
    private ResourceLocation[] requirements = new ResourceLocation[0];
    private ISpecialRules special = (w, p, a) -> true;
    private String toLocalise = "";
    private int offsetY = -1;
    private long tickTime = 15L;
    private int width;
    private int length;
    private boolean canHaveMultiple;

    public Building() {}
    public Building(ResourceLocation resource){
        setRegistryName(resource);
        toLocalise = resource.getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + resource.getResourcePath().toLowerCase(Locale.ENGLISH);
        REGISTRY.register(this);
    }
    /** Set the requirements for this building from string values
     * @param requirements  the buildings required in format "modid:building".
     *                      you can exclude the modid if the building is from harvestfestival
     * @return the building */
    public Building setRequirements(String... requirements) {
        this.requirements = new ResourceLocation[requirements.length];
        for (int i = 0; i < requirements.length; i++) {
            this.requirements[i] = getResourceFromName(requirements[i]);
        }

        return this;
    }

    /** Internal helper method for converting from string to resource
     *  @param resource string name **/
    private ResourceLocation getResourceFromName(String resource) {
        if (resource.contains(":")) return new ResourceLocation(resource);
        else return new ResourceLocation("harvestfestival", resource);
    }

    /** Sets how many ticks between each block the builder will place
     * @param time the amount of ticks between each block being placed
     * @return the building*/
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
        Collections.addAll(inhabitants, npcs);
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

    public Collection<NPC> getInhabitants() {
        return inhabitants;
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



    public ResourceLocation[] getRequirements() {
        return requirements;
    }

    public boolean canHaveMultiple() {
        return canHaveMultiple;
    }

    public int getWidth() {
        return width;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getLength() {
        return length;
    }

    /** Called when the festival changes
     * @param world         the world object
     * @param pos           the position of the building
     * @param rotation      the rotation of the building
     * @param oldFestival   the previous festival
     * @param newFestival   the new festival
     */
    public void onFestivalChanged(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Rotation rotation, @Nonnull Festival oldFestival, @Nonnull Festival newFestival) {}

    /** Called when the building has finished being built
     * @param world         the world object
     * @param pos           the position of the building
     * @param rotation      the rotation of the building
     */
    public void onBuilt(World world, BlockPos pos, Rotation rotation) {}

    @Override
    public boolean equals(Object o) {
        return o instanceof Building && getRegistryName() != null && getRegistryName().equals(((Building) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }
}

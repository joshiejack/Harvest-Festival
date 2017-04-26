package joshie.harvest.api.buildings;

import com.google.common.collect.Maps;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.core.HFRegistry;
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

import javax.annotation.Nonnull;
import java.util.*;

public class Building extends HFRegistry<Building> {
    public static final Map<ResourceLocation, Building> REGISTRY = Maps.newHashMap();
    private final Set<NPC> inhabitants = new HashSet<>();
    private ResourceLocation[] requirements = new ResourceLocation[0];
    private ISpecialRules special = (w, p, a) -> true;
    private String toLocalise = "";
    private int offsetY = -1;
    private long tickTime = 15L;
    private int width;
    private int length;
    private boolean canHaveMultiple;

    public Building(ResourceLocation resource){
        super(resource);
        toLocalise = resource.getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + resource.getResourcePath().toLowerCase(Locale.ENGLISH);
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

    @Override
    public final Map<ResourceLocation, Building> getRegistry() {
        return REGISTRY;
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
            toLocalise = getResource().getResourceDomain().toLowerCase(Locale.ENGLISH) + ".structures." + getResource().getResourcePath().toLowerCase(Locale.ENGLISH);
        }

        return I18n.translateToLocal(toLocalise);
    }

    @SuppressWarnings("unused")
    public boolean isSuitableLocation(BuildingKey key) {
        return true;
    }

    @Nonnull
    public ItemStack getBlueprint() {
        return HFApi.buildings.getBlueprint(this);
    }

    @Nonnull
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
        return o instanceof Building && getResource() != null && getResource().equals(((Building) o).getResource());
    }

    @Override
    public int hashCode() {
        return getResource() == null? 0 : getResource().hashCode();
    }
}
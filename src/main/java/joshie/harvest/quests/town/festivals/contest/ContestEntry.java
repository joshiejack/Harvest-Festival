package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.quests.town.festivals.Place;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ContestEntry {
    private final UUID player;
    private final UUID entity;
    private final NPC npc;
    private final int stall;

    public ContestEntry(UUID player, UUID entity, int stall) {
        this.player = player;
        this.entity = entity;
        this.npc = null;
        this.stall = stall;
    }

    public ContestEntry(NPC npc, UUID entity, int stall) {
        this.player = null;
        this.entity = entity;
        this.npc = npc;
        this.stall = stall;
    }

    @Nullable
    private AnimalStats getStats(World world) {
        EntityAnimal animal  = EntityHelper.getAnimalFromUUID(world, entity);
        if (animal != null) {
            return EntityHelper.getStats(animal);
        } else return null;
    }

    @Nullable
    private EntityPlayer getPlayer() {
        return EntityHelper.getPlayerFromUUID(player);
    }

    @SuppressWarnings("ConstantConditions")
    public int getScore(World world) {
        EntityAnimal animal  = EntityHelper.getAnimalFromUUID(world, entity);
        int score = 0;
        if (animal != null) {
            AnimalStats stats = EntityHelper.getStats(animal);
            score += stats.getHappiness(); //Base level
            //Add bonuses, if the animal has had everything done today
            if (stats.performTest(AnimalTest.CAN_CLEAN)) {
                if (stats.performTest(AnimalTest.HAS_EATEN)) score += 1000;
                if (stats.performTest(AnimalTest.HAD_TREAT)) score += 3000;
                if (stats.performTest(AnimalTest.IS_CLEAN)) score += 3000;
                if (stats.performTest(AnimalTest.BEEN_LOVED)) score += 2000;
            } else {
                if (stats.performTest(AnimalTest.HAS_EATEN)) score += 1000;
                if (stats.performTest(AnimalTest.HAD_TREAT)) score += 5000;
                if (stats.performTest(AnimalTest.BEEN_LOVED)) score += 3000;
            }

            //Reduce the score if the animal is sick
            if (stats.performTest(AnimalTest.IS_SICK)) {
                score -= 25000;
            }
        }

        return score;
    }

    public String getOwnerName() {
        EntityPlayer player = getPlayer();
        return player != null ? player.getName() : npc != null ? npc.getLocalizedName() : "Anonymous";
    }

    public String getEntityName(World world) {
        EntityAnimal animal = getEntity(world);
        return animal != null ? animal.getName() : "Mystery Animal";
    }

    @Nullable
    public EntityAnimal getEntity(World world) {
        return EntityHelper.getAnimalFromUUID(world, entity);
    }

    @Nullable
    public UUID getUUID() {
        return player;
    }

    @Nonnull
    public UUID getAnimal() {
        return entity;
    }

    public int getStall() {
        return stall;
    }

    public String getName(World world) {
        EntityAnimal animal = EntityHelper.getAnimalFromUUID(world, entity);
        return animal == null ? "" : animal.getName();
    }

    public void reward(World world, Place place, NPC[] npcs, ItemStack reward) {
        AnimalStats stats = getStats(world);
        EntityPlayer player = getPlayer();
        if (stats != null && player != null) { //Give the rewards for this
            SpawnItemHelper.addToPlayerInventory(player, reward);
            stats.affectHappiness(place.happiness); //Make the animal happier, and the npcs that took part v
            for (NPC npc: npcs) {
                HFApi.player.getRelationsForPlayer(player).affectRelationship(npc, place.happiness);
            }
        } else if (npc != null) {
            EntityAnimal animal = getEntity(world);
            if (animal != null) {
                List<EntityNPC> npcList = EntityHelper.getEntities(EntityNPC.class, world, new BlockPos(animal), 64D, 64D);
                for (EntityNPC aNPC: npcList) {
                    if (aNPC.getNPC() == npc) {
                        aNPC.setHeldItem(EnumHand.OFF_HAND, reward);
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    public static ContestEntry fromNBT(NBTTagCompound tag) {
        UUID animal = UUID.fromString(tag.getString("Animal"));
        Integer stall = tag.getInteger("Stall");
        if (tag.hasKey("Player")) {
            UUID player = UUID.fromString(tag.getString("Player"));
            return new ContestEntry(player, animal, stall);
        } else if (tag.hasKey("NPC")) {
            NPC npc = NPC.REGISTRY.getValue(new ResourceLocation(tag.getString("NPC")));
            return new ContestEntry(npc, animal, stall);
        } else return null;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Animal", entity.toString());
        tag.setInteger("Stall", stall);
        if (player != null) tag.setString("Player", player.toString());
        else if (npc != null) tag.setString("NPC", npc.getRegistryName().toString());
        return tag;
    }
}

package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.knowledge.HFNotes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchasableEntity implements IPurchasable {
    private final String resource;
    private final ItemStack product;
    private final Class<? extends Entity> eClass;
    private final long cost;
    private Note note;
    private boolean loaded;
    private boolean carry;

    /**
     * If lead is true, entity spawns with a lead, otherwise, entity spawns mounting the player
     **/
    public PurchasableEntity(Class<? extends Entity> clazz, long cost, ItemStack render) {
        this.product = render;
        this.eClass = clazz;
        this.cost = cost;
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
    }

    public PurchasableEntity setNote(Note note) {
        this.note = note;
        return this;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return amount == 1;
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public ItemStack getDisplayStack() {
        return product;
    }

    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + product.getDisplayName());

        //Calculate if this is carriable animal
        if (!loaded) {
            loaded = true;
            EntityAnimal animal = createEntity(MCClientHelper.getWorld());
            AnimalStats stats = EntityHelper.getStats(animal);
            if (stats != null && stats.performTest(AnimalTest.CAN_CARRY)) carry = true;
        }

        if (carry) {
            list.add(TextHelper.translate("check.head"));
        } else list.add(TextHelper.translate("check.lead"));
    }

    public EntityAnimal createEntity(World world) {
        EntityAnimal entity = null;
        try {
            if (eClass != null) {
                entity = (EntityAnimal) eClass.getConstructor(new Class[]{World.class}).newInstance(world);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return entity;
    }

    @Override
    public void onPurchased(EntityPlayer aPlayer) {
        if (!aPlayer.world.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP)aPlayer;
            EntityAnimal theEntity = createEntity(player.world);
            if (theEntity != null) {
                theEntity.setPosition(player.posX, player.posY, player.posZ);
                AnimalStats stats = EntityHelper.getStats(theEntity);
                if (stats != null) {
                    if (stats.performTest(AnimalTest.CAN_CARRY)) {
                        if (player.getPassengers().size() == 0) theEntity.startRiding(player, true);
                    } else theEntity.setLeashedToEntity(player, true);
                }

                //Spawn the entity
                player.world.spawnEntity(theEntity);

                //Notify the client
                if (stats != null) {
                    if (stats.performTest(AnimalTest.CAN_CARRY)) {
                        player.connection.sendPacket(new SPacketSetPassengers(player));
                    } else player.connection.sendPacket(new SPacketEntityAttach(theEntity, theEntity.getLeashedToEntity()));
                }
            }
        }

        if (note != null) HFApi.player.getTrackingForPlayer(aPlayer).learnNote(note);
        HFApi.player.getTrackingForPlayer(aPlayer).learnNote(HFNotes.ANIMAL_HAPPINESS);
        HFApi.player.getTrackingForPlayer(aPlayer).learnNote(HFNotes.ANIMAL_STRESS);
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}
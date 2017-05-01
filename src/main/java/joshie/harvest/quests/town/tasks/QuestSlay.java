package joshie.harvest.quests.town.tasks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.town.Town;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestDaily;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@HFQuest("slay")
public class QuestSlay extends QuestDaily {
    private static final String[] list = new String[] { "harvestfestival.dark_chick", "harvestfestival.dark_chicken", "harvestfestival.dark_sheep", "harvestfestival.dark_cow" };
    private int targetAmount = 1;
    private String targetMob = "harvestfestival.dark_chick";
    private int counter;

    public QuestSlay() {
        super(HFNPCs.MINER);
    }

    @Override
    public int getDaysBetween() {
        return 3;
    }

    @Override
    public void onQuestActivated() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        EntityPlayer player = getPlayerFromSource(event.getSource());
        if (player != null) {
            if (counter < targetAmount && isValidKill(event.getEntityLiving())) {
                counter++; //Increase the counter
                syncData(player);
            }
        }
    }

    @Nullable
    private EntityPlayer getPlayerFromSource(DamageSource damage) {
        Entity source = damage.getSourceOfDamage();
        if (!(source instanceof EntityPlayer)) {
            source = damage.getEntity();
        }

        return source instanceof EntityPlayer ? (EntityPlayer) source : null;
    }

    private boolean isValidKill(EntityLivingBase entity) {
        return EntityList.isStringEntityName(entity, targetMob);
    }

    @Override
    public String getDescription(World world, @Nullable EntityPlayer player) {
        if (player != null) {
            if (targetAmount - counter == 0) return getLocalized("talk");
            return getLocalized("desc", targetAmount - counter, TextHelper.localize("entity." + targetMob + ".name"));
        } else return getLocalized("task", targetAmount, TextHelper.localize("entity." + targetMob + ".name"), 1000L * targetAmount);
    }

    @Override
    public void onSelectedAsDailyQuest(Town town, World world, BlockPos pos) {
        rand.setSeed(HFApi.calendar.getDate(world).hashCode());
        int position = rand.nextInt(list.length);
        targetMob = list[position];
        targetAmount = 1 + rand.nextInt(10);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPCEntity entity) {
        return super.isNPCUsed(player, entity) && targetAmount != 0 && counter >= targetAmount;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        return TextHelper.getRandomSpeech(entity.getNPC(), "harvestfestival.quest.slay.complete", 32);
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        complete(player);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getRelationsForPlayer(player).affectRelationship(HFNPCs.MINER, 2500);
        rewardGold(player, 1000L * targetAmount);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        counter = nbt.getByte("Counter");
        targetAmount = nbt.getByte("TargetAmount");
        targetMob = nbt.getString("TargetMob");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("Counter", (byte) counter);
        nbt.setByte("TargetAmount", (byte) targetAmount);
        nbt.setString("TargetMob", targetMob);
        return super.writeToNBT(nbt);
    }
}

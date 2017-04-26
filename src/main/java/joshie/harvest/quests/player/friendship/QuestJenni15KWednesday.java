package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendshipStore;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownBuilding;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

import static joshie.harvest.api.calendar.Season.AUTUMN;
import static joshie.harvest.api.calendar.Season.SUMMER;

@HFQuest("friendship.jenni.wednesday")
public class QuestJenni15KWednesday extends QuestFriendshipStore {
    public QuestJenni15KWednesday() {
        super(HFNPCs.GS_OWNER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JENNI_10K);
    }

    @Override
    protected Quest getQuest() {
        return Quests.OPEN_WEDNESDAYS;
    }

    @Override
    protected NonNullList<ItemStack> getRewardStacks(EntityPlayer player) {
        Season season = HFApi.calendar.getDate(player.world).getSeason();
        if (season == SUMMER) return NonNullList.withSize(1, HFCrops.PINEAPPLE.getSeedStack(2));
        else if (season == AUTUMN) return NonNullList.withSize(1, HFCrops.GREEN_PEPPER.getSeedStack(2));
        else return NonNullList.withSize(1, HFCrops.CABBAGE.getSeedStack(2));
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        if (HFApi.quests.getCurrentQuests(player).contains(getQuest())) {
            List<ItemStack> stacks = getRewardStacks(player);
            if (stacks != null) {
                for (ItemStack stack : stacks) {
                    rewardItem(player, stack);
                }
            }
        } else {
            //Update the signs for the general store
            HFApi.quests.completeQuestConditionally(getQuest(), player);
            TownBuilding building = TownHelper.getClosestTownToEntity(player, false).getBuilding(HFBuildings.SUPERMARKET);
            if (building != null) {
                World world = player.world;
                BlockPos pos = building.pos.up(2);
                if (building.rotation == Rotation.CLOCKWISE_90) { //North
                    pos = pos.offset(EnumFacing.WEST, 12).offset(EnumFacing.SOUTH, 12);
                } else if (building.rotation == Rotation.COUNTERCLOCKWISE_90) { //South
                    pos = pos.offset(EnumFacing.EAST, 12).offset(EnumFacing.NORTH, 12);
                } else if (building.rotation == Rotation.NONE) {
                    pos = pos.offset(EnumFacing.EAST, 12).offset(EnumFacing.SOUTH, 12);
                } else if (building.rotation == Rotation.CLOCKWISE_180) {
                    pos = pos.offset(EnumFacing.WEST, 12).offset(EnumFacing.NORTH, 12);
                }

                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileEntitySign) {
                    TileEntitySign sign = ((TileEntitySign) tile);
                    sign.signText[1] = new TextComponentString("Monday-Friday");
                    sign.markDirty();
                    IBlockState state = world.getBlockState(sign.getPos());
                    world.notifyBlockUpdate(sign.getPos(), state, state, 3);
                }
            }
        }
    }
}

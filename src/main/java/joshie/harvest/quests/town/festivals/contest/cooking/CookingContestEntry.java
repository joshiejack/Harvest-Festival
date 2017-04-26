package joshie.harvest.quests.town.festivals.contest.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.tile.TilePlate;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.quests.town.festivals.QuestContestCooking;
import joshie.harvest.quests.town.festivals.contest.ContestEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class CookingContestEntry extends ContestEntry<QuestContestCooking> {
    @Nonnull
    private final ItemStack stack;
    private final BlockPos pos;
    private final Utensil category;

    public CookingContestEntry(UUID player, BlockPos pos, @Nonnull ItemStack stack, int stall) {
        super(player, stall);
        this.stack = stack;
        this.pos = pos;
        this.category = getUtensilFromStack(stack);
    }

    public CookingContestEntry(NPC npc, BlockPos pos, @Nonnull ItemStack stack, int stall) {
        super(npc, stall);
        this.stack = stack;
        this.pos = pos;
        this.category = getUtensilFromStack(stack);
    }

    private Utensil getUtensilFromStack(ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY.values()) {
            if (recipe.getStack().isItemEqual(stack)) return recipe.getUtensil();
        }

        return null;
    }

    @Override
    public int getScore(QuestContestCooking quest, World world) {
        int hunger = ((ItemFood)stack.getItem()).getHealAmount(stack);
        float saturation = ((ItemFood)stack.getItem()).getSaturationModifier(stack);
        long gold = HFApi.shipping.getSellValue(stack);
        return (int)(hunger * saturation + gold) - ((category == quest.getCategory()) ? 0: 1000);
    }

    @Override
    public String getName(World world) {
        return stack.getDisplayName();
    }

    @Override
    public String getTextFromScore(String unlocalised, int score) {
        return TextHelper.localize(unlocalised + "." + Math.max(0, Math.min(9, (int)Math.floor(((double)score) / 1000))));
    }

    public boolean isInvalid(World world) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TilePlate) {
            ItemStack contents = ((TilePlate)tile).getContents();
            return !contents.isItemEqual(stack);
        } else return true;
    }

    @Override
    public void reward(World world, Place place, NPC[] npcs, @Nonnull ItemStack reward) {
        EntityPlayer player = getPlayer(world);
        if (player != null) { //Give the rewards for this
            SpawnItemHelper.addToPlayerInventory(player, reward);
            for (NPC npc: npcs) {
                HFApi.player.getRelationsForPlayer(player).affectRelationship(npc, place.happiness);
            }
        } else if (npc != null) {
            List<EntityNPC> npcList = EntityHelper.getEntities(EntityNPC.class, world, pos, 64D, 64D);
            for (EntityNPC aNPC: npcList) {
                if (aNPC.getNPC() == npc) {
                    aNPC.setHeldItem(EnumHand.OFF_HAND, reward);
                    break;
                }
            }
        }
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tag = super.toNBT();
        tag.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
        tag.setLong("Pos", pos.toLong());
        return tag;
    }

    @Nullable
    public static CookingContestEntry fromNBT(NBTTagCompound tag) {
        ItemStack stack = new ItemStack(tag.getCompoundTag("Stack"));
        BlockPos pos = BlockPos.fromLong(tag.getLong("Pos"));
        Integer stall = tag.getInteger("Stall");
        if (tag.hasKey("Player")) {
            UUID player = UUID.fromString(tag.getString("Player"));
            return new CookingContestEntry(player, pos, stack, stall);
        } else if (tag.hasKey("NPC")) {
            NPC npc = NPC.REGISTRY.get(new ResourceLocation(tag.getString("NPC")));
            return new CookingContestEntry(npc, pos, stack, stall);
        } else return null;
    }
}

package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingPlacement;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.item.ActionGiftItem;
import uk.joshiejack.settlements.entity.ai.action.tasks.ActionDestroy;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemDestroy extends ItemSingular {
    public ItemDestroy() {
        super(new ResourceLocation(Settlements.MODID, "destroy"));
        setCreativeTab(Settlements.TAB);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            BuildingPlacement placement = BuildingPlacement.onActivated(Building.NULL, player);
            if (placement != null && placement.getMode() == BuildingPlacement.BuildingPlacementMode.PLACED) {
                TownServer town = TownFinder.getFinder(world).findOrCreate(player, placement.getPosition());
                EntityNPC npc = town.getCensus().getSpawner().byOccupation(world, "builder", placement.getPosition());
                if (npc != null) {
                    npc.getPhysicalAI().addToEnd(new ActionDestroy(placement.toTownBuilding()));
                    npc.getPhysicalAI().addToEnd(new ActionGiftItem().withPlayer(player).withData(AdventureItems.BLUEPRINT.getStackFromResource(placement.toTownBuilding().getBuilding().getRegistryName())));
                }


                stack.splitStack(1);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}

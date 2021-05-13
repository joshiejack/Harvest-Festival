package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingPlacement;
import uk.joshiejack.settlements.network.town.land.PacketAddBuilding;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import uk.joshiejack.penguinlib.item.interfaces.IPenguinItem;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.template.Template;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBuilding extends ItemMultiMap<Building> implements IPenguinItem {
    public ItemBuilding() {
        super(new ResourceLocation(Settlements.MODID, "building"), Building.REGISTRY);
        setCreativeTab(Settlements.TAB);
        setMaxStackSize(1);
    }

    @Override
    protected Building getNullEntry() {
        return Building.NULL;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Building building = getObjectFromStack(stack);
        if (building != null) {
            if (!world.isRemote) {
                Template template = building.getTemplate();
                BuildingPlacement placement = BuildingPlacement.onActivated(building, player);
                if (placement != null && placement.getMode() == BuildingPlacement.BuildingPlacementMode.PLACED) {
                    Town<?> town = TownFinder.getFinder(world).findOrCreate(player, placement.getPosition());
                    TownBuilding townBuilding = placement.toTownBuilding().setBuilt();
                    town.getLandRegistry().addBuilding(world, townBuilding);
                    PenguinNetwork.sendToEveryone(new PacketAddBuilding(world.provider.getDimension(), town.getID(), townBuilding));
                    template.placeBlocks(world, placement.getPosition(), placement.getRotation()); //Place the blocks AFTER creating the town
                    stack.splitStack(1);
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}

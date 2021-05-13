package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingPlacement;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.tasks.ActionBuild;
import uk.joshiejack.settlements.network.town.land.PacketAddBuilding;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemBlueprint extends ItemMultiMap<Building> {
    public ItemBlueprint() {
        super(new ResourceLocation(Settlements.MODID, "blueprint"), Building.REGISTRY);
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
                BuildingPlacement placement = BuildingPlacement.onActivated(building, player);
                if (placement != null && placement.getMode() == BuildingPlacement.BuildingPlacementMode.PLACED) {
                    TownServer town = TownFinder.getFinder(world).findOrCreate(player, placement.getPosition());
                    EntityNPC npc = town.getCensus().getSpawner().byOccupation(world, "builder", placement.getOriginalPosition()); //Spawn the builder where you click
                    if (npc != null) {
                        TownBuilding townBuilding = placement.toTownBuilding();
                        town.getLandRegistry().addBuilding(world, townBuilding);
                        PenguinNetwork.sendToEveryone(new PacketAddBuilding(world.provider.getDimension(), town.getID(), townBuilding));
                        npc.getPhysicalAI().addToEnd(new ActionBuild(building, placement.getPosition(), placement.getRotation()));
                    }

                    stack.splitStack(1);
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }
}

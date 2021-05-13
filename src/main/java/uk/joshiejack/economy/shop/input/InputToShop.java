package uk.joshiejack.economy.shop.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.ShopHelper;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class InputToShop {
    public static final Multimap<ShopInputBlockState, Department> BLOCKSTATE_TO_SHOP = HashMultimap.create();
    public static final Multimap<ShopInputEntity, Department> ENTITY_TO_SHOP = HashMultimap.create();
    public static final Multimap<ShopInputItem, Department> ITEM_TO_SHOP = HashMultimap.create();

    public static void register(String type, String data, Department department) {
        if (type.equals("block")) BLOCKSTATE_TO_SHOP.get(new ShopInputBlockState(StateAdapter.fromString(data))).add(department);
        else if (type.equals("entity")) ENTITY_TO_SHOP.get(new ShopInputEntity(new ResourceLocation(data))).add(department);
        else if (type.equals("item")) ITEM_TO_SHOP.get(new ShopInputItem(StackHelper.getStackFromString(data))).add(department);
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;
        ShopInputBlockState input = new ShopInputBlockState(event.getWorld().getBlockState(event.getPos()));
        ShopHelper.open(BLOCKSTATE_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getEntityPlayer(), event.getItemStack(), input),
                event.getEntityPlayer().isSneaking() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getWorld().isRemote) return;
        ShopInputEntity input = new ShopInputEntity(event.getTarget());
        ShopHelper.open(ENTITY_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getTarget(), event.getEntityPlayer(), event.getItemStack(), input),
                event.getEntityPlayer().isSneaking() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isRemote) return;
        ShopInputItem input = new ShopInputItem(event.getItemStack());
        ShopHelper.open(ITEM_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getEntityPlayer(), event.getItemStack(), input),
                event.getEntityPlayer().isSneaking() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }
}

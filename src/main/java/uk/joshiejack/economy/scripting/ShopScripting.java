package uk.joshiejack.economy.scripting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.scripting.wrappers.DepartmentJS;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.ShopHelper;
import uk.joshiejack.economy.shop.input.InputMethod;
import uk.joshiejack.economy.shop.input.ShopInputEntity;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.EntityJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;

import javax.annotation.Nonnull;
import java.util.Objects;

import static uk.joshiejack.economy.Economy.MODID;
import static uk.joshiejack.economy.shop.input.InputToShop.ENTITY_TO_SHOP;

@Mod.EventBusSubscriber(modid = MODID)
public class ShopScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("shops", ShopScripting.class);
    }

    @SuppressWarnings("unchecked")
    private static ShopTarget target(PlayerJS playerW, EntityJS<?> npcW) {
        Entity entity = npcW.penguinScriptingObject;
        EntityPlayer player = playerW.penguinScriptingObject;
        return new ShopTarget(entity.world, entity.getPosition(), entity,
                player, player.getHeldItemMainhand(), new ShopInputEntity(entity));
    }

    @SuppressWarnings("unchecked")
    public static void open(PlayerJS player, EntityJS<?> npc) {
        ShopTarget target = target(player, npc);
        ShopHelper.open(ENTITY_TO_SHOP.get((ShopInputEntity) target.input), target, InputMethod.SCRIPT);
    }

    @SuppressWarnings("unchecked")
    public static void open(PlayerJS player, EntityJS<?> npc, String id) {
        Department shop = Department.REGISTRY.get(id);
        if (shop != null) {
            shop.open(target(player, npc));
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean canOpen(PlayerJS player, EntityJS<?> npc, String id) {
        Department department = Department.REGISTRY.get(id);
        return department != null && department.isValidFor(target(player, npc), Condition.CheckType.SHOP_IS_OPEN);
    }

    @SuppressWarnings("unchecked")
    public static boolean has(PlayerJS player, EntityJS<?> npc) {
        ShopTarget target = target(player, npc);
        return ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((ShopInputEntity) target.input), target, Condition.CheckType.SHOP_EXISTS, InputMethod.SCRIPT) != null;
    }

    public static boolean isOpen(PlayerJS player, EntityJS<?> npc) {
        ShopTarget target = target(player, npc);
        return ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((ShopInputEntity) target.input), target, Condition.CheckType.SHOP_IS_OPEN, InputMethod.SCRIPT) != null;
    }

    @SuppressWarnings("unchecked")
    public static DepartmentJS get(PlayerJS player, @Nonnull EntityJS<?> npc) {
        ShopTarget target = target(player, npc);
        return WrapperRegistry.wrap(Objects.requireNonNull(ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((ShopInputEntity) target.input), target, Condition.CheckType.SHOP_EXISTS, InputMethod.SCRIPT)));
    }

    public static DepartmentJS get(String name) {
        Department department = Department.REGISTRY.get(name);
        return department == null ? null : WrapperRegistry.wrap(department);
    }
}

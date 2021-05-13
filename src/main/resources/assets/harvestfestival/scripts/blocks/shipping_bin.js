function onEntityCollision(world, pos, state, entity) {
    if (!world.isClient() && entity.is("item")) {
        var item = entity.item();
        var thrower = entity.thrower();
        if (thrower != null && shipping.value(item) > 0) {
            shipping.ship(thrower, item, item.count());
            entity.kill();
        }
    }
}

function getInventorySize() {
    return 1;
}

function isStackValidForInsertion(slot, stack) {
    return shipping.value(stack) > 0;
}

function isSlotValidForExtraction(slot, amount) {
    return false;
}

function onRightClicked(player, hand) {
    var stack = player.getHeldItem(hand);
    if (shipping.value(stack) > 0) {
        if (!player.world().isClient()) {
            shipping.ship(player, stack, 1);
        }

        return true;
    } else return false;
}
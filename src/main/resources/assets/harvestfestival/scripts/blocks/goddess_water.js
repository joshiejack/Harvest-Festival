function onEntityCollision(world, pos, state, entity) {
    if (!world.isClient() && entity.is("item") && !entity.data().has("Ungiftable")) {
        var item = entity.item();
        var thrower = entity.thrower();
        if (thrower != null) {
            var town = towns.find(world, pos);
            if (town.id() != 0) {
                var goddess = settlements.getOrSpawnNPCAt(thrower, entity.pos(), "harvestfestival:harvest_goddess");
                goddess.disableItemDrops(); //Girl you don't need to drop your flower
                if (scripting.isTrue("harvestfestival:buttons_gift_item", "canGiveGift", goddess, thrower)) {
                    if (scripting.isTrue("harvestfestival:buttons_gift_item", "onGiftGiven", goddess, thrower, item)) {
                        item.shrink(1);
                        if (item.isEmpty()) {
                            entity.kill();
                        }
                    } else entity.data().save("Ungiftable", true);
                }
            }
        }
    }
}
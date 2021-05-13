//When the goddess flower expires inside of water, spawn a harvest goddess or teleport the existing one to the flowers location
function onItemExpire(entity, original) {
print("expired")
    if (!entity.world().isClient()) {
        var item = entity.item();
        var player = entity.thrower();
         print(item)
        if (item.is("harvestfestival:flower#goddess")) {
            print("was goddess flower")
            var extend = false;
            var goddess;
            if (player != null && entity.isInsideOf("water")) {
                 var goddess = settlements.getOrSpawnNPCAt(player, entity.pos(), "harvestfestival:harvest_goddess");
                 print(goddess)
                 if (goddess.existed() == 0) {
                    goddess.chat(this_id, player);//Force a conversation if we have just spawned her
                 } else extend = true;
            } else extend = true;

            //Extend the flower if the goddess already existed or we aren't going in to water
            if (extend && !entity.data().has("Extended")) {
                entity.data().save("Extended", true);
                return 5950;
            }
        }
    }

    return original;
}

//Convert any flower to the goddess flower if applicable
function onRightClickBlock(player, pos, item) {
    if (item.is("stickWood")) {
        var world = player.world();
        if (!world.isClient()) {
            var category = gifts.category(world.getState(pos).item());
            if (category.name() == "flower") { //If the item is registered as a flower
                //If we are next to water
                for (var x = -1; x <= 1; x++) {
                    for (var z = -1; z <= 1; z++) {
                        if (world.getState(pos.offset(x, -1, z)).block() == "minecraft:water") {
                            //Display particle
                            world.displayParticle(villager_happy, 32, (pos.x() + 0.5) + (Math.random() - 0.5), pos.y() + 0.5 + 0.2 * Math.random(), (pos.z() + 0.5) + (Math.random() - 0.5), 0);
                            if (random(1, 32) == 16) {
                                world.playSound(pos, "harvestfestival:goddess", neutral, 0.5, 1.1);
                                world.setState(getState("harvestfestival:flower[flower=goddess]"), pos);
                            }

                            return;
                        }
                    }
                }
            }
        }
    }
}
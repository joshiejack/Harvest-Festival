var types1 = ["oak", "spruce", "birch", "jungle"];
var types2 = ["dark_oak", "acacia"];

function onHarvestDrop(event) {
    if (!event.world().isClient()) {
        var state = event.state();
        if (state.isLeaves(event.world(), event.pos())) {
            //Remove sapling and apple drops
            event.remove("treeSapling");
            event.remove("cropApple");

            if (!event.player().isHolding("minecraft:shears", main_hand)) {
                if (random(1, 12) == 7) {
                    event.add(createStack("minecraft:stick").setCount(random(1, 2)));
                }

                if (random(1, 8) == 3) {
                    //Now to add our own
                    var block = event.state().block();
                    if (block === "minecraft:leaves") {
                        for (var i = 0; i < types1.length; i++) {
                             var type = types1[i];
                             if (event.state().is("variant", type)) {
                                event.add(createStack("harvestfestival:seedling " + i))
                             }
                        }
                    } else if (block === "minecraft:leaves2") {
                        for (var i = 0; i < types1.length; i++) {
                             var type = types2[i];
                             if (event.state().is("variant", type)) {
                                event.add(createStack("harvestfestival:seedling " + (4 + i)))
                             }
                        }
                    }
                }
            }
        }
    }
 }